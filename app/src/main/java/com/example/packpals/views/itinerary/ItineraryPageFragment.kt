package com.example.packpals.views.itinerary

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.packpals.R
import com.example.packpals.viewmodels.ItineraryPageViewModel
import com.example.packpals.views.Notification
import com.example.packpals.views.channelID
import com.example.packpals.views.messageExtra
import com.example.packpals.views.notificationID
import com.example.packpals.views.titleExtra
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.random.Random

@AndroidEntryPoint
class ItineraryPageFragment : Fragment() {
    private val viewModel: ItineraryPageViewModel by activityViewModels()
    private var notificationPermissionGranted = false
    private var alarmPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createNotificationChannel()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_itinerary_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayout = requireView().findViewById<LinearLayout>(R.id.lliterinerary)

        val notificationManager = requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        var notificationButton = view.findViewById<TextView>(R.id.notificationButton)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getNotificationPermission()
        }
        if (alarmPermissionGranted && notificationPermissionGranted) {
            notificationButton.setOnClickListener{
                notificationManager.cancelAll()
                viewModel.itineraryItemsList.let { itineraryItems ->
                    for (item in itineraryItems.value!!) {
                        if (item.startDate?.time!! > Date().time) {
                            scheduleNotification(
                                viewModel.getCurrentTripId()!!,
                                "Location: " + item.location + "\n" +
                                        "Weather: " + item.forecast + "\n" +
                                        "Time: " + getHourAndMinutes(item.startDate!!) +
                                        " - " + getHourAndMinutes(item.endDate!!),
                                item.startDate!!
                            )
                        }
                    }
                }
            }
        } else {
            notificationButton.visibility = GONE
        }

        viewModel.itineraryItemsList.observe(viewLifecycleOwner) { itineraryItems ->
            linearLayout.removeAllViews()
            for (item in itineraryItems) {
                val itineraryView = LayoutInflater.from(context).inflate(R.layout.view_itenerary_item, linearLayout, false)

                itineraryView.findViewById<TextView>(R.id.tvlocation).text = item.location
                itineraryView.findViewById<TextView>(R.id.tvforecast).text = item.forecast
                itineraryView.findViewById<TextView>(R.id.tvdate).text = SimpleDateFormat("MM/dd/yyyy").format(item.startDate)

                if (item.photo_reference != null){
                    Glide
                        .with(requireContext())
                        .load(item.photo_reference)
                        .into(itineraryView.findViewById(R.id.image))
                }else{
                    itineraryView.findViewById<ImageView>(R.id.image).setImageResource(R.drawable.ic_itinerary)
                }


                itineraryView.setOnClickListener {
                    viewModel.setCurrentItem(item)
                    viewModel.setAdd(false)
                    findNavController().navigate(R.id.action_itineraryFragment_to_itemDetailsPageFragment)
                }

                linearLayout.addView(itineraryView)
            }
        }

        val addNewItemButton = requireView().findViewById<Button>(R.id.addItemButton)
        addNewItemButton.setOnClickListener {
            findNavController().navigate(R.id.action_itineraryFragment_to_addItineraryItemFragment)
        }

    }
    private fun scheduleNotification(title: String, message: String, date: Date)
    {
        val intent = Intent(requireContext(), Notification::class.java)
        val title = title
        val message = message
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            (0..Int.MAX_VALUE).random(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime(date)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(title, message)
    }

    private fun showAlert(title: String, message: String)
    {

        AlertDialog.Builder(requireContext())
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message)
            .setPositiveButton("Okay"){_,_ ->}
            .show()
    }

    private fun getTime(date: Date): Long
    {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel()
    {
        val name = "Notification Channel"
        val desc = "Itinerary Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun getNotificationPermission() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && ContextCompat.checkSelfPermission(requireActivity().applicationContext,
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                PERMISSIONS_REQUEST_POST_NOTIFICATIONS
            )
        } else {
            notificationPermissionGranted = true
        }
        if (!alarmManager.canScheduleExactAlarms()) {
            Intent().also { intent ->
                intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                requireContext().startActivity(intent)
            }
        } else {
            alarmPermissionGranted = true
        }

    }

    private fun getHourAndMinutes (date:Date):String {
        val cal = Calendar.getInstance()
        cal.time = date
        val hours = cal.get(Calendar.HOUR_OF_DAY)
        val minutes = cal.get(Calendar.MINUTE)
        return ""+hours+":"+minutes
    }


    companion object {
        private const val PERMISSIONS_REQUEST_POST_NOTIFICATIONS = 2
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ItineraryPageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}