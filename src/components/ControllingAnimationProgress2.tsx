import React, {useCallback, useEffect, useRef, useState} from 'react';
import {Animated, Easing, SafeAreaView, Text, View} from 'react-native';
import LottieView from 'lottie-react-native';
import myAnimation from './animation2.json';

const AnimatedLottieView = Animated.createAnimatedComponent(LottieView);

export default function ControllingAnimationProgress2() {
  const animationProgress = useRef(new Animated.Value(0));
  // const [basetime, setBasetime] = useState(2000);

  const animeControl = (basetime: number) =>
    Animated.sequence([
      Animated.timing(animationProgress.current, {
        toValue: 1,
        duration: basetime * 0.6,
        useNativeDriver: false,
        easing: Easing.linear,
      }),
      Animated.timing(animationProgress.current, {
        toValue: 0,
        duration: basetime * 0.4,
        useNativeDriver: false,
        easing: Easing.linear,
      }),
    ]);

  useEffect(() => {
    let basetime = 2000;
    // myAnimation.layers.map(l => {
    //   console.log(l.nm);
    // });
    const interval = setInterval(() => {
      animeControl(basetime).start();
    }, basetime);
    return () => clearInterval(interval);
  }, []);

  // useEffect(() => {
  //   const interval2 = setInterval(() => {
  //     console.log('basetime decreased by 100', basetime);
  //     setBasetime(basetime - 100);
  //   }, 1000);
  //   return () => clearInterval(interval2);
  // }, [basetime]);

  return (
    <AnimatedLottieView
      // source={require('./animation2.json')}
      source={myAnimation}
      colorFilters={[
        {
          keypath: 'Heart Outlines',
          color: '#221b5e',
        },
        {
          keypath: 'Heart Outlines 2',
          color: '#881940',
        },
        {
          keypath: 'Heart Outlines 3',
          color: '#9aaf3a',
        },
      ]}
      progress={animationProgress.current}
      style={{
        width: '100%',
        height: '100%',
        // backgroundColor: 'black',
        position: 'absolute',
      }}
    />
  );
}
