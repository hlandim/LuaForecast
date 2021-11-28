# LUAS Forecasting
RIM Employee LUAS App


Architecture
MVVM

- Communications between View <-> ViewModel are made using Android Jetpack Compose and LiveData. 
- Using [Retrofit](https://square.github.io/retrofit/) library to create interfaces with [LUAS Forecasting API](https://data.gov.ie/dataset/luas-forecasting-api).
- Using [Mockito](https://github.com/mockito/mockito) library to create unit tests.
- Using [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for Dependencies injection.
- Handling network connection lost.
