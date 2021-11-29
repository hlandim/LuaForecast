# LUAS Forecasting
RIM Employee LUAS App


Architecture
MVVM

- Communications between View <-> ViewModel are made using Android Jetpack Compose and LiveData. 
- Using [Retrofit](https://square.github.io/retrofit/) library to create interfaces with [LUAS Forecasting API](https://data.gov.ie/dataset/luas-forecasting-api).
- Using [Mockito](https://github.com/mockito/mockito) library to create integration tests.
- Using [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for Dependencies injection.
- Using [Jectpack Compose](https://developer.android.com/jetpack/compose/) to create layout and automated test.
- Handling network connection lost.
