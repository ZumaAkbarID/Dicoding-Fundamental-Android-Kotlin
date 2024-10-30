package com.rwa.submissionakhirdicodingevent.ui.setting

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.rwa.submissionakhirdicodingevent.databinding.FragmentSettingBinding
import com.rwa.submissionakhirdicodingevent.ui.ViewModelFactory
import com.rwa.submissionakhirdicodingevent.worker.EventWorker
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(context, "Notifications permission rejected", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        val switchTheme = binding.darkModeSwitch
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val settingViewModel: SettingViewModel by viewModels {
            factory
        }

        settingViewModel.getThemeSettings()
            .observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }

        val switchReminder = binding.dailyReminderSwitch
        workManager = WorkManager.getInstance(requireActivity())

        switchReminder.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }

            settingViewModel.saveReminderSetting(isChecked)
            if (isChecked) {
                settingViewModel.getFirstActiveEvent().observe(viewLifecycleOwner) { (name, desc) ->
                    storeEventData(name, desc)
                    startPeriodicTask()
                }
            } else {
                cancelPeriodicTask()
            }
        }

        settingViewModel.getReminderSetting().observe(viewLifecycleOwner) { isReminder: Boolean ->
            switchReminder.isChecked = isReminder
        }

        return binding.root
    }

    private fun storeEventData(title: String, description: String) {
        val sharedPreferences =
            requireActivity().getSharedPreferences("EventData", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("title", title)
            putString("description", description)
            apply()
        }
    }

    private fun startPeriodicTask() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest = PeriodicWorkRequest.Builder(EventWorker::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()
        workManager.enqueue(periodicWorkRequest)
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(viewLifecycleOwner) { workInfo ->
                val status = workInfo.state.name
                Toast.makeText(context, "Status : $status", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cancelPeriodicTask() {
        workManager.cancelWorkById(periodicWorkRequest.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}