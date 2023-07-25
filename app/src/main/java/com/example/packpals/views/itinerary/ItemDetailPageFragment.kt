package com.example.packpals.views.itinerary

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.packpals.R
import com.example.packpals.viewmodels.ItineraryPageViewModel
import com.example.packpals.viewmodels.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
@AndroidEntryPoint
class ItemDetailPageFragment : Fragment() {
    private val viewModel: ItineraryPageViewModel by activityViewModels()
    private var startDate: LocalDate? = null
    private val photoViewModel: PhotoViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_details_page, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentItem.observe(viewLifecycleOwner) { item ->
            val tvLocationField = view.findViewById<TextView>(R.id.nameField)
            val tvForecastField = view.findViewById<TextView>(R.id.forecastField)
            val tvAddressField = view.findViewById<TextView>(R.id.addressField)

            tvLocationField.text = item.location
            tvAddressField.text = item.address
            tvForecastField.text = item.forecast

            tvLocationField.setOnClickListener{
                showNameEditDialog()
            }

            tvAddressField.setOnClickListener{
                showAddressEditDialog()
            }
        }

        viewModel.startDate.observe(viewLifecycleOwner){date ->
            val tvStartDateField = view.findViewById<TextView>(R.id.startDateField)
            val tvStartTimeField = view.findViewById<TextView>(R.id.startTimeField)

            if (date != null) {
                tvStartDateField.text = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                tvStartTimeField.text = date.format(DateTimeFormatter.ofPattern("HH:mm"))
            }

        }

        viewModel.endDate.observe(viewLifecycleOwner){date ->
            val tvEndDateField = view.findViewById<TextView>(R.id.endDateField)
            val tvEndTimeField = view.findViewById<TextView>(R.id.endTimeField)

            if (date != null) {
                tvEndDateField.text = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                tvEndTimeField.text = date.format(DateTimeFormatter.ofPattern("HH:mm"))
            }
        }


        val startDatePicker = view.findViewById<Button>(R.id.startDateField)
        startDatePicker.setOnClickListener {
            showStartDatePicker()
        }
        val endDatePicker = view.findViewById<Button>(R.id.endDateField)
        endDatePicker.setOnClickListener {
            showEndDatePicker()
        }
        val startTimePicker = view.findViewById<Button>(R.id.startTimeField)
        startTimePicker.setOnClickListener{
            showStartTimePicker()
        }
        val endTimePicker = view.findViewById<Button>(R.id.endTimeField)
        endTimePicker.setOnClickListener{
            showEndTimePicker()
        }
        val saveButton = view.findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener{
            lifecycleScope.launch{
                viewModel.createItem(view.findViewById<TextView>(R.id.nameField).text as String)
                if (viewModel.add.value == true){
                    photoViewModel.createAlbum(view.findViewById<TextView>(R.id.nameField).text as String)
                }
                viewModel.fetchItineraryItems()
                findNavController().navigate(R.id.action_itemDetailsPageFragment_to_itineraryPageFragment)
            }
        }

    }
    private fun showNameEditDialog() {
        val editText = EditText(requireContext())
        editText.setText(view?.findViewById<TextView>(R.id.nameField)?.text)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Text")
            .setView(editText)
            .setMessage(view?.findViewById<TextView>(R.id.nameField)?.text)
            .setPositiveButton("Save") { dialog, _ ->
                val newText = editText.text.toString()
                view?.findViewById<TextView>(R.id.nameField)?.text = newText
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showAddressEditDialog() {
        val editText = EditText(requireContext())
        editText.setText(view?.findViewById<TextView>(R.id.addressField)?.text)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Text")
            .setView(editText)
            .setPositiveButton("Save") { dialog, _ ->
                val newText = editText.text.toString()
                view?.findViewById<TextView>(R.id.addressField)?.text = newText
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showStartDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val localDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0)
                viewModel.setStartDate(localDate)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showEndDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val localDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0)
                viewModel.setEndDate(localDate)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showStartTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                // Handle the time selected by the user
                viewModel.setStartTime(selectedHour, selectedMinute)
            },
            hour,
            minute,
            true // Set to true for 24-hour mode, false for 12-hour mode
        )

        timePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showEndTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                // Handle the time selected by the user
                viewModel.setEndTime(selectedHour, selectedMinute)
            },
            hour,
            minute,
            true // Set to true for 24-hour mode, false for 12-hour mode
        )

        timePickerDialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ItemDetailPageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
