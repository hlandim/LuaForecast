# RIM Employee LUAS App

Architecture used: MVVM + Dependencie Injenction + Jectpeack Compose.

- Communications between View <-> ViewModel are made using Android Jetpack Compose and LiveData. 
- Using [Retrofit](https://square.github.io/retrofit/) library to create interfaces with [LUAS Forecasting API](https://data.gov.ie/dataset/luas-forecasting-api).
- Using [Mockito](https://github.com/mockito/mockito) library to create integration tests.
- Using [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for Dependencies injection.
- Using [Jectpack Compose](https://developer.android.com/jetpack/compose/) to create layout and automated test.
- Handling network connection lost.

### Images

| Marlborough (Dark Theme) | Stillorgan (Light Theme)  |
|---|---|
| <img src="https://github.com/hlandim/LuaForecast/blob/master/imgs/Marlborough.png" width="300"/>    |  <img src="https://github.com/hlandim/LuaForecast/blob/master/imgs/Stillorgan.png" width="300"/> |
