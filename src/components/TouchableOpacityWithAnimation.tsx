import React, {useState} from 'react';
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
  const [count, setCount] = useState(0);
  const onPress = () => setCount(prevCount => prevCount + 1);

  return (
    <SafeAreaView style={{width: '100%', height: '100%'}}>
      <SurroundingAnimation
        style={styles.backLottieAnimation}
      />
      <HeartBeat
        style={styles.backLottieAnimation}
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
