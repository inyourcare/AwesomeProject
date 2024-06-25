import React, {useCallback, useEffect, useRef, useState} from 'react';
import {Animated, Easing, StyleProp, ViewStyle} from 'react-native';
import LottieView from 'lottie-react-native';
import myAnimation from './animation3.json';

const AnimatedLottieView = Animated.createAnimatedComponent(LottieView);

export default function ControllingAnimationProgress2({style}: {style: StyleProp<ViewStyle>}) {
  const animationProgress = useRef(new Animated.Value(0));
  const [basetime, setBasetime] = useState(500);

  const animeControl = (basetime: number) =>
    Animated.loop(
      Animated.sequence([
        Animated.timing(animationProgress.current, {
          toValue: 1,
          duration: basetime,
          useNativeDriver: false,
          easing: Easing.linear,
        }),
        // Animated.timing(animationProgress.current, {
        //   toValue: 0,
        //   duration: basetime * 0.4,
        //   useNativeDriver: false,
        //   easing: Easing.linear,
        // }),
      ]),
    );

  useEffect(() => {
    const interval = setInterval(() => {
      animeControl(basetime).start();
    }, basetime);
    return () => clearInterval(interval);
  }, [basetime]);

  // useEffect(() => {
  //   const timeout = setTimeout(() => {
  //     console.log('setTimeout');
  //     setBasetime(1000);
  //   }, 10000);
  //   return () => clearTimeout(timeout);
  // }, []);
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
      style={style}
    />
  );
}
