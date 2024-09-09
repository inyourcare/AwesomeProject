import React, {useEffect, useRef, useState} from 'react';
import {
  DeviceEventEmitter,
  NativeModules,
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import HeartBeat from './animation/heart';
import SurroundingAnimation from './animation/surrounding';

export default function TouchableOpacityWithAnimation() {
  // const [basetime, setBasetime] = useState(500);
  // const [count, setCount] = useState(0);
  // const onPress = () => setCount(prevCount => prevCount + 1);

  const isCallBack = useRef(false)
  const onPress = () => {
    console.log('hi');
    const {MobiCareModule} = NativeModules;
    console.log(MobiCareModule);
    MobiCareModule.devicesScanStart('A019719');
  };
  const onPress2 = () => {
    console.log('hi2');
    const {MobiCareModule} = NativeModules;
    console.log(MobiCareModule);
    MobiCareModule.stopHeartRate();
  };

  const connectDevices = () => {
    console.log('connectDevices')
    const { MobiCareModule } = NativeModules
    MobiCareModule.connectDevices()
  }

  useEffect(() => {
    const {MobiCareModule} = NativeModules;
    const emitter1 = DeviceEventEmitter.addListener(
      'BluetoothDataEvent',
      params => {
        isCallBack.current = true;
        console.log('BluetoothDataEvent', params)
        connectDevices();
      },
    );
    const emitter2 = DeviceEventEmitter.addListener(
      'BluetoothHeartRateEvent',
      params => {
        console.log('BluetoothHeartRateEvent', params)
      },
    )
    const emitter3 = DeviceEventEmitter.addListener(
      'BatteryStatusEvent',
      params => {
        console.log('BatteryStatusEvent', params)
        MobiCareModule.getHeartRate()
      },
    )
    const emitter4 = DeviceEventEmitter.addListener(
      'BluetoothConnectEvent',
      params => {
        console.log('BluetoothConnectEvent', params)
      },
    )
    return () => {
      emitter1.remove();
      emitter2.remove();
      emitter3.remove();
      emitter4.remove();
    };
  }, []);

  // useEffect(() => {
    // const interval2 = setInterval(() => {
    //   console.log('setInterval', basetime);
    //   if (basetime === 500) {
    //     setBasetime(1500);
    //   } else if (basetime === 1500) {
    //     setBasetime(1000);
    //   } else if (basetime === 1000) {
    //     setBasetime(500);
    //   }
    // }, 10000);
    // return () => clearInterval(interval2);
  // }, [basetime]);

  return (
    <SafeAreaView style={{width: '100%', height: '100%'}}>
      {/* <SurroundingAnimation
        style={styles.backLottieAnimation}
      /> */}
      {/* <HeartBeat
        style={styles.backLottieAnimation}
        basetime={basetime}
      /> */}
      <View style={styles.container}>
        {/* <View style={styles.countContainer}>
          <Text>Count: {count}</Text>
        </View> */}
        <TouchableOpacity style={styles.button} onPress={onPress}>
          <Text>start</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.button} onPress={onPress2}>
          <Text>stop</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    paddingHorizontal: 10,
    // position: 'absolute'
    // zIndex: 100
  },
  button: {
    alignItems: 'center',
    backgroundColor: '#DDDDDD',
    padding: 10,
    margin:5
  },
  countContainer: {
    alignItems: 'center',
    padding: 10,
  },
  backLottieAnimation: {
    width: '100%',
    height: '100%',
    // backgroundColor: 'black',
    position: 'absolute',
    opacity: 0.3,
  },
});
