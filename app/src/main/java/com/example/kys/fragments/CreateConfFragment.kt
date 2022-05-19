package com.example.kys.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import com.example.kys.R
import com.example.kys.databinding.FragmentCreateConfBinding
import kotlinx.android.synthetic.main.fragment_create_conf.view.*


class CreateConfFragment : Fragment(R.layout.fragment_create_conf) {
    private var _binding: FragmentCreateConfBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        binding.apply {
            val durations = resources.getStringArray(R.array.durations)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.conf_duration, durations)
            binding.confDurationTextView.setAdapter(arrayAdapter)
        }
        binding.apply {
            confRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == R.id.confRadioButton2) {
                    confOnlineEditText.isEnabled = false
                    confAddressEditText.isEnabled = true
                }
                if (checkedId == R.id.confRadioButton1) {
                    confAddressEditText.isEnabled = false
                    confOnlineEditText.isEnabled = true
                }
                if (checkedId == R.id.confRadioButton3) {
                    confAddressEditText.isEnabled = true
                    confOnlineEditText.isEnabled = true
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateConfBinding.bind(view)

        binding.apply {
            confDateTextInputLayout.setEndIconOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        confDateTextInputLayout.confDateEditText.setText(date)
                    }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }
        }

        binding.apply {
            confHourTextInputLayout.setEndIconOnClickListener {
                val timePickerFragment = TimePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY1",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY1") {
                        val time = bundle.getString("SELECTED_TIME")
                        confHourTextInputLayout.confHourEditText.setText(time)
                    }
                }

                timePickerFragment.show(supportFragmentManager, "TimePickerFragment")
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}