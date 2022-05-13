package com.example.kys.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kys.R
import com.example.kys.databinding.FragmentCreateConfBinding
import kotlinx.android.synthetic.main.fragment_create_conf.view.*


class CreateConfFragment : Fragment(R.layout.fragment_create_conf) {
    private var _binding: FragmentCreateConfBinding? = null
    private val binding get() = _binding!!

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}