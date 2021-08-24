package com.example.kitchenflow.ui.main

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.kitchenflow.R
import com.example.kitchenflow.data.entity.SortType
import com.example.kitchenflow.data.entity.getSortTypeString
import com.example.kitchenflow.databinding.MainFragmentBinding
import org.koin.android.ext.android.inject
import java.util.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by inject()
    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.model = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.orders.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                adapter.addAll(it.filter { orderModel ->
                    val scheduled = "${orderModel.scheduledFor.month}${orderModel.scheduledFor.date}"
                    val current = "${viewModel.date.value?.month}${viewModel.date.value?.date}"
                    scheduled == current
                })
                viewModel.currentOrders.set(adapter.itemCount.toString())
                setErrorVisibility(adapter.itemCount == 0)
            }
        }
    }

    private fun setErrorVisibility(visible: Boolean) {
        with(binding.noOrders) {
            if (visible) {
                imageView.visibility = View.VISIBLE
                title.visibility = View.VISIBLE
                title.text = if (viewModel.isRestaurantOpen()) {
                    requireContext().getString(R.string.no_orders_yet)
                } else {
                    requireContext().getString(R.string.restaurant_closed)
                }
            } else {
                imageView.visibility = View.GONE
                title.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        with(binding) {
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_variations,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        when (parent?.getItemAtPosition(position) as String) {
                            resources.getSortTypeString(SortType.PICKUP) -> {
                                this@MainFragment.adapter.sort(SortType.PICKUP)
                            }
                            resources.getSortTypeString(SortType.PREPARATION) -> {
                                this@MainFragment.adapter.sort(SortType.PREPARATION)
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            }
            adapter = OrdersAdapter(requireContext())
            ordersListRv.adapter = adapter
            searchBarEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val filter = s.toString()
                    if (filter.isNotEmpty()) {
                        viewModel.sortItems(s.toString())?.let {
                            adapter.addAll(it.filter { orderModel ->
                                val scheduled = "${orderModel.scheduledFor.month}${orderModel.scheduledFor.date}"
                                val current = "${viewModel.date.value?.month}${viewModel.date.value?.date}"
                                scheduled == current
                            })
                        }
                    } else viewModel.orders.value?.let { adapter.addAll(it) }
                    viewModel.currentOrders.set(adapter.itemCount.toString())
                    setErrorVisibility(adapter.itemCount == 0)
                }

                override fun afterTextChanged(s: Editable?) {}
            })
            dateTv.setOnClickListener {
                val modelYear = model?.date?.value?.year
                val modelMonth = model?.date?.value?.month
                val modelDay = model?.date?.value?.date
                if (modelYear != null && modelMonth != null && modelDay != null) {
                    DatePickerDialog(
                        requireContext(),
                        { _, year, month, dayOfMonth ->
                            model?.updateDate(Date(year, month, dayOfMonth))
                        },
                        modelYear,
                        modelMonth,
                        modelDay
                    ).show()
                }
            }
        }
    }
}