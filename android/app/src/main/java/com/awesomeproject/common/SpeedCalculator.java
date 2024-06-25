// package com.awesomeproject.common;

// import java.util.LinkedList;
// import java.util.Queue;

// public class SpeedCalculator {
//     private Queue<Float> speeds;

//     public SpeedCalculator() {
//         this.speeds = new LinkedList<>();
//     }

//     public double addSpeed(float speed) {
//         if (speeds.size() == 5) {
//             speeds.poll(); // 가장 오래된 속도 값 제거
//         }
//         speeds.offer(speed); // 새로운 속도 값 추가

//         if (speeds.size() == 5) { // 속도 값이 3개일 때 평균 계산
//             double average = speeds.stream().mapToDouble(Float::doubleValue).average().orElse(0.0);
//             // 소수점 첫째 자리까지의 평균 속도를 반올림
//             double roundedAverage = Math.round(average * 10) / 10.0;
//             return roundedAverage;
//         }
//         return 0.0;
//     }
// }
