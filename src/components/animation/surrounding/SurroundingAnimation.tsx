import React, {useEffect, useRef} from 'react';
import LottieView from 'lottie-react-native';
import {Text} from 'react-native';
import myAnimation from './animation.json';

export default function Animation() {
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
      style={{
        width: '100%',
        height: '100%',
        position: 'absolute',
        opacity: 0.3,
      }}
      autoPlay
      loop
    />
  );
}
