import React, { useEffect, useRef } from 'react';
import LottieView from 'lottie-react-native';
import {Text} from 'react-native';
//https://github.com/lottie-react-native/lottie-react-native
export default function Animation() {
  const animationRef = useRef<LottieView>(null);

  useEffect(() => {
    // animationRef.current?.play();

    // Or set a specific startFrame and endFrame with:
    animationRef.current?.play(0, 120);
  }, []);
  return (
    <>
      <Text>Hello World!</Text>
      <LottieView
        ref={animationRef}
        source={require('./animation2.json')}
        style={{width: '100%', height: '100%'}}
        // autoPlay
        // loop
      />
      <Text>Hello World!</Text>
    </>
  );
}
