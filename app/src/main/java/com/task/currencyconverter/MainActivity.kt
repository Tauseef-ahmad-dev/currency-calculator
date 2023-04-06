package com.task.currencyconverter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.task.currencyconverter.core.data.Resource
import com.task.currencyconverter.core.domain.model.History
import com.task.currencyconverter.core.ui.HistoryAdapter
import com.task.currencyconverter.core.utils.Helper.makeStatusBarTransparent
import com.task.currencyconverter.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: CurrencyViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var historyAdapter: HistoryAdapter
    private var currentSpBefore = ""
    private var currentSpAfter = ""

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar?.hide()
        makeStatusBarTransparent()

        lifecycle.addObserver(viewModel)
        historyAdapter = HistoryAdapter()
        showTextLoading(true)
        historyAdapter.onItemClick = { selectedData ->
            Log.e("MainActivity", "onItemClick $selectedData")
            viewModel.deleteHistory(selectedData.id)
        }

        binding.tvClearAll.setOnClickListener {
            viewModel.deleteAllHistory()
        }

        with(binding.rvHistory) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = historyAdapter
        }

        val beforeStream = RxTextView.textChanges(binding.etBefore)
            .skipInitialValue()
            .map { query ->
                query.toString()
            }
            .debounce(400, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())

        beforeStream.subscribe({
            getExchangeCall(it)
        }, {
            Log.e("BeforeStream", "subscribe: ${it.message}")
        })

        eventSpinnerHandling()
        observeSpinner()
        showHistory()
    }


    private fun showHistory() {
        viewModel.history.observe(this) {
            if (it != null) {
                hideRecent(it.isEmpty())
                historyAdapter.setData(it)
            }
        }
    }

    private fun hideRecent(isEmpty: Boolean) {
        if (isEmpty) {
            binding.textView.visibility = View.INVISIBLE
            binding.tvClearAll.visibility = View.INVISIBLE
        } else {
            binding.textView.visibility = View.VISIBLE
            binding.tvClearAll.visibility = View.VISIBLE
        }

    }

    private fun observeSpinner() {
        viewModel.currency.observe(this) { code ->
            if (code != null) {
                when (code) {
                    is Resource.Success -> {
                        val list = ArrayList<String>()
                        code.data?.map {
                            list.add(it.code)
                        }
                        Log.e("Observe", "List: $list")
                        arrayAdapter = ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_spinner_dropdown_item,
                            list
                        )
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        binding.apply {
                            spAfter.adapter = arrayAdapter
                            spBefore.adapter = arrayAdapter
                        }
                    }
                    is Resource.Loading -> {
                        Log.e("Observe", "List: loading")

                    }
                    is Resource.Error -> {
                        Log.e("Observe", "List: failed")
                    }

                }
            }

        }

    }

    private fun eventSpinnerHandling() {
        binding.spBefore.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                showTextLoading(true)
                getExchangeCall(binding.etBefore.text.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.spAfter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                showTextLoading(true)
                getExchangeCall(binding.etBefore.text.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    private fun getExchangeCall(it: String?) {
        if (it?.isEmpty() == true || it == null) {
            binding.etAfter.setText("")
        } else {
            viewModel.getExchangeCall(
                binding.spBefore.selectedItem.toString(),
                binding.spAfter.selectedItem.toString()
            ).enqueue(object : Callback<String> {
                @SuppressLint("CheckResult")
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("Response", "${response.body()}")
                    if (it == "0") {
                        binding.etAfter.setText("0")
                    } else {
                        var converted = it.toDouble().times(response.body()?.toDouble()!!)
                        val df = DecimalFormat("#.###")
                        df.roundingMode = RoundingMode.CEILING
                        converted = df.format(converted.toBigDecimal()).toDouble()
                        binding.etAfter.setText(converted.toString())
                        val history = History(
                            fromCode = binding.spBefore.selectedItem.toString(),
                            toCode = binding.spAfter.selectedItem.toString(),
                            fromValue = it.toDouble(),
                            toValue = converted
                        )
                        Completable.fromAction {
                            viewModel.insertHistory(history)
                        }.subscribeOn(Schedulers.io())
                            .subscribe()
                        showHistory()
                    }

                    if (currentSpAfter != binding.spAfter.selectedItem.toString()
                        || currentSpBefore != binding.spBefore.selectedItem.toString()
                    ) {
                        currentSpBefore = binding.spBefore.selectedItem.toString()
                        currentSpAfter = binding.spAfter.selectedItem.toString()
                        val df = DecimalFormat("#.####")
                        df.roundingMode = RoundingMode.CEILING
                        val basicCurrency =
                            "1.0 $currentSpBefore - ${
                                df.format(
                                    response.body()?.toBigDecimal()
                                )
                            } $currentSpAfter"
                        showTextLoading(false)
                        binding.tvBasicCurrency.text = basicCurrency
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("Failure", "${t.message}")
                }

            })

        }

    }

    private fun showTextLoading(isLoading: Boolean) {
        binding.aniLoading.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        binding.tvBasicCurrency.visibility = if(isLoading) View.INVISIBLE else View.VISIBLE
    }
}