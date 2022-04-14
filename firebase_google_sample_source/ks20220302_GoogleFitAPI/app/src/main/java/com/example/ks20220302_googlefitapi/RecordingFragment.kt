package com.example.ks20220302_googlefitapi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ks20220302_googlefitapi.databinding.FragmentRecordingBinding
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType

const val RECORD_TAG = "BasicRecordingApi"

class RecordingFragment : Fragment() {
    private var mBinding: FragmentRecordingBinding? = null
    private val binding get()                       = mBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = FragmentRecordingBinding.inflate(inflater, container, false)

        subscribe()

        binding.btnLiveLocation.setOnClickListener { subscribe()              }
        binding.btnLiveStepCount.setOnClickListener { dumpSubscriptionsList()  }
        binding.btnWeight.setOnClickListener { cancelSubscription()     }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelSubscription()
    }

    /**
     * 구독 시작
     * :: 가능한 모든 데이터 소스에서 데이터 타입 구독 가능
     * :: 특정 데이터소스만 사용하는 것도 가능
     */
    private fun subscribe() {
        Fitness.getRecordingClient(requireContext(), (activity as MainActivity).getGoogleAccount())
            .subscribe(DataType.TYPE_CALORIES_EXPENDED) // 소비된 칼로리 수
            //.subscribe(DataType.TYPE_STEP_COUNT_DELTA)  // 시간 간격 동안의 총 단계수??
            //.subscribe(DataType.TYPE_WEIGHT)            // 체중 kg
            .addOnSuccessListener {
                val msg = "구독 성공!"
                Log.i(RECORD_TAG, msg)
                binding.titleTextView.text = msg
            }
            .addOnFailureListener {
                val msg = "구독 실패"
                Log.i(RECORD_TAG, msg)
                binding.titleTextView.text = msg
            }

    }

    /**
     * 구독 목록 호출
     */
    private fun dumpSubscriptionsList() {
        Fitness.getRecordingClient(requireContext(), (activity as MainActivity).getGoogleAccount())
            //.listSubscriptions(DataType.TYPE_STEP_COUNT_DELTA)
            .listSubscriptions(DataType.TYPE_CALORIES_EXPENDED)
            .addOnSuccessListener { subscriptions ->
                var msg = "dumpSubscriptionsList() 호출"
                for (subscription in subscriptions) {
                    val dataType = subscription.dataType!!
                    msg += "\n활성화 된 구독 data type: ${dataType.name}"
                    msg += "\n활성화 된 구독 data fields: ${dataType.fields[0]}"
                    Log.i(RECORD_TAG, "활성화 된 구독 data type: ${dataType.name}")
                }

                if (subscriptions.isEmpty()) {
                    msg += "\n활성화 된 구독이 없음"
                    Log.i(RECORD_TAG, "활성화 된 구독이 없음")
                }

                binding.titleTextView.text = msg
            }
    }

    /**
     * 구독 취소
     */
    private fun cancelSubscription() {
        val dataTypeStr = DataType.TYPE_CALORIES_EXPENDED.toString()
        Log.i(RECORD_TAG, "Unsubscribing from data type: $dataTypeStr")

        Fitness.getRecordingClient(requireContext(), (activity as MainActivity).getGoogleAccount())
            .unsubscribe(DataType.TYPE_CALORIES_EXPENDED)
            .addOnSuccessListener {
                val msg = "구독 취소 성공 - data type: $dataTypeStr"
                Log.i(RECORD_TAG, msg)
                binding.titleTextView.text = msg
            }
            .addOnFailureListener {
                val msg = "구독 취소 실패 - data type: $dataTypeStr"
                Log.i(RECORD_TAG, msg)
                binding.titleTextView.text = msg
            }

    }
}