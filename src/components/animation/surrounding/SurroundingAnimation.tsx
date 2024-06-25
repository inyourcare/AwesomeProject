import React, {useEffect, useRef} from 'react';
import LottieView from 'lottie-react-native';
import {StyleProp, ViewStyle} from 'react-native';
import myAnimation from './animation.json';
export default function Animation({style}: {style: StyleProp<ViewStyle>}) {
  const animationRef = useRef<LottieView>(null);

  useEffect(() => {
    animationRef.current?.play();

    // Or set a specific startFrame and endFrame with:
    // animationRef.current?.play(0, 120);
  }, []);
  return (
    <LottieView
      ref={animationRef}
      source={myAnimation}
      style={style}
      autoPlay
      loop
    />
  );
}
