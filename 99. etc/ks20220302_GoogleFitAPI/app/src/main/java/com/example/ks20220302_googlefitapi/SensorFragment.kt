package com.example.ks20220302_googlefitapi

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ks20220302_googlefitapi.databinding.FragmentSensorBinding
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataSourcesRequest
import com.google.android.gms.fitness.request.OnDataPointListener
import com.google.android.gms.fitness.request.SensorRequest
import java.util.concurrent.TimeUnit

const val SENSOR_TAG = "BasicSensorsApi"

class SensorFragment : Fragment() {
    private var mBinding: FragmentSensorBinding? = null
    private val binding get()                    = mBinding!!

    private var dataPointListener: OnDataPointListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = FragmentSensorBinding.inflate(inflater, container, false)

        binding.btn01.setOnClickListener {
            if (dataPointListener != null) {
                unregisterFitnessDataListener()
            }
            findFitnessDataSources(DataType.TYPE_LOCATION_SAMPLE)
            binding.tvRunData.text = ""
        }
        binding.btn02.setOnClickListener {
            if (dataPointListener != null) {
                unregisterFitnessDataListener()
            }
            findFitnessDataSources(DataType.TYPE_STEP_COUNT_DELTA)
            binding.tvRunData.text = ""
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterFitnessDataListener()
    }

    /**
     * 장치 및 컴패니언 장치에서 사용 가능한 데이터 소스 나열
     */
    private fun findFitnessDataSources(dataType: DataType) { // [START find_data_sources]
        Fitness.getSensorsClient(requireContext(), (activity as MainActivity).getGoogleAccount())
            .findDataSources(
                DataSourcesRequest.Builder()
                    .setDataTypes(dataType)  // 주어진 순간에 사용자 위치 나타냄
                    //.setDataTypes(DataType.TYPE_STEP_COUNT_DELTA)
                    .setDataSourceTypes(DataSource.TYPE_RAW)
                    .build())
            .addOnSuccessListener { dataSources ->
                for (dataSource in dataSources) {
                    // Data source found: DataSource{raw:Application{com.google.android.gms}:Device{samsung:SM-G965N:1c8c31cc:1:2}:
                    // live_location:DataType{com.google.location.sample[latitude(f), longitude(f), accuracy(f), altitude(f)]}}
                    Log.i(SENSOR_TAG, "Data source found: $dataSource")
                    // Data Source type: com.google.location.sample
                    Log.i(SENSOR_TAG, "Data Source type: " + dataSource.dataType.name)

                    // 데이터를 받을 수신 리스너 등록
                    if (dataSource.dataType == dataType && dataPointListener == null) {
                        Log.i(SENSOR_TAG, "리스너 등록 중")
                        registerFitnessDataListener(dataSource, dataType)
                    }
                }
            }
            .addOnFailureListener { e -> Log.e(SENSOR_TAG, "failed", e) }
    }

    /**
     * 특정 데이터 소스에서 원시 데이터를 수신하도록 리스너 추가 :: SensorsClient.add
     */
    @SuppressLint("SetTextI18n")
    private fun registerFitnessDataListener(dataSource: DataSource, dataType: DataType) {
        dataPointListener = OnDataPointListener { dataPoint ->
            binding.tvRun.text = "위치 이동 감지"
            binding.tvRun.setTextColor(Color.RED)

            var textAdd = ""

            for (field in dataPoint.dataType.fields) {
                val value = dataPoint.getValue(field)
                Log.i(SENSOR_TAG, "Detected DataPoint field: ${field.name}")
                Log.i(SENSOR_TAG, "Detected DataPoint value: $value")
                textAdd += "${field.name}: $value\n"
            }
            binding.tvRunData.text = textAdd
            Log.e(SENSOR_TAG, "tv : $textAdd")

            binding.tvRun.text = "센서 데이터 감지 중..."
            binding.tvRun.setTextColor(Color.BLACK)
        }
        Fitness.getSensorsClient(requireContext(), (activity as MainActivity).getGoogleAccount())
            .add(
                SensorRequest.Builder()
                    .setDataSource(dataSource)
                    .setDataType(dataType)
                    .setSamplingRate(10, TimeUnit.SECONDS)
                    .build(),
                dataPointListener!!
            )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(SENSOR_TAG, "리스너 등록 완료!")
                } else {
                    Log.e(SENSOR_TAG, "리스너 등록 실패", task.exception)
                }
            }
    }

    /**
     *  리스너 삭제 함수
     */
    private fun unregisterFitnessDataListener() {
        if (dataPointListener == null) {
            return
        }
        Fitness.getSensorsClient(requireContext(), (activity as MainActivity).getGoogleAccount())
            .remove(dataPointListener!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result!!) {
                    Log.i(SENSOR_TAG, "리스너 지워짐")
                } else {
                    Log.i(SENSOR_TAG, "리스너 지워지지 않음")
                }
            }
        dataPointListener = null
    }
}