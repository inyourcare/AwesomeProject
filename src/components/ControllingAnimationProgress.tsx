import React, {useEffect, useRef} from 'react';
import {Animated, Easing} from 'react-native';
import LottieView from 'lottie-react-native';

const AnimatedLottieView = Animated.createAnimatedComponent(LottieView);

export default function ControllingAnimationProgress() {
  const animationProgress = useRef(new Animated.Value(0));
  const basetime = 1000
  const loopConfig:Animated.LoopAnimationConfig = {
    
  }

  useEffect(() => {
    // Animated.timing(animationProgress.current, {
    //   toValue: 1,
    //   duration: 1000,
    //   easing: Easing.linear,
    //   useNativeDriver: false,
    // }).start(({finished})=>{
    //   console.log(finished)
    // });
    Animated.loop(
      Animated.sequence([
        Animated.timing(animationProgress.current, {
          toValue: 1,
          duration: basetime,
          useNativeDriver: true,
          easing: Easing.linear
        }),
        Animated.timing(animationProgress.current, {
          toValue: 0,
          duration: basetime * .8,
          useNativeDriver: true,
          easing: Easing.linear
        })
      ])
    ).start();
  }, []);

  return (
    <AnimatedLottieView
      source={require('./animation2.json')}
      progress={animationProgress.current}
      style={{width: '100%', height: '100%'}}
    />
  );
}
