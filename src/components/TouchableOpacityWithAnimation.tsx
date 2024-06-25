import React, {useEffect, useState} from 'react';
import {
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import HeartBeat from './animation/heart';
import SurroundingAnimation from './animation/surrounding';

export default function TouchableOpacityWithAnimation() {
  const [basetime, setBasetime] = useState(500);
  const [count, setCount] = useState(0);
  const onPress = () => setCount(prevCount => prevCount + 1);

  useEffect(() => {
    const interval2 = setInterval(() => {
      console.log('setInterval', basetime);
      if (basetime === 500) {
        setBasetime(1500);
      } else if (basetime === 1500) {
        setBasetime(1000);
      } else if (basetime === 1000) {
        setBasetime(500);
      }
    }, 10000);
    return () => clearInterval(interval2);
  }, [basetime]);

  return (
    <SafeAreaView style={{width: '100%', height: '100%'}}>
      <SurroundingAnimation
        style={styles.backLottieAnimation}
      />
      <HeartBeat
        style={styles.backLottieAnimation}
        basetime={basetime}
      />
      <View style={styles.container}>
        <View style={styles.countContainer}>
          <Text>Count: {count}</Text>
        </View>
        <TouchableOpacity style={styles.button} onPress={onPress}>
          <Text>Press Here</Text>
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
  }
});
