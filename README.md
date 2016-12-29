# Digital Currency Converter
### Widget for Android Coming Soon

This project is currently designed to retrieve Bitcoin ticker information from OKCoin.cn exchange and perform the following conversion from CNY to USD. The following information will be displayed on a widget: low price, high price, last price, volume, and when the widget was last updated.

#### Installation
Coming Soon.

#### API Calls
1. Bitcoin ticker information (in CNY): https://www.okcoin.cn/about/rest_api.do

  ##### Request 
  ``` 
  GET https://www.okcoin.cn/api/v1/ticker.do?symbol=btc_cny
  ```
  ##### Response
  ``` 
  {  
     "date":"1475194700",
     "ticker":{  
        "buy":"4043.16",
        "high":"4056.0",
        "last":"4043.2",
        "low":"4023.0",
        "sell":"4043.2",
        "vol":"1019144.2997"
     }
  }
  ```
  ##### Return Values
  ```
  date: server time for returned data
  buy: best bid
  high: highest price
  last: latest price
  low: lowest price
  sell: best ask
  vol: volume (in the last rolling 24 hours)
  ```

2. Exchange Rates: http://fixer.io/

  #### Request
  ```
  GET http://api.fixer.io/latest?base=USD
  ```
  #### Response
  ```
  {
    "base": "USD",
    "date": "2016-09-29",
    "rates": {
        "AUD": 1.3049,
        "BGN": 1.743,
        "BRL": 3.235,
        "CAD": 1.3085,
        "CHF": 0.96925,
        "CNY": 6.6679,
        "CZK": 24.082,
        "DKK": 6.6414,
        "GBP": 0.76765,
        "HKD": 7.7552,
        "HRK": 6.6975,
        "HUF": 275.06,
        "IDR": 12975,
        "ILS": 3.7569,
        "INR": 66.866,
        "JPY": 101.49,
        "KRW": 1100.8,
        "MXN": 19.424,
        "MYR": 4.1235,
        "NOK": 8.0566,
        "NZD": 1.377,
        "PHP": 48.33,
        "PLN": 3.8379,
        "RON": 3.9685,
        "RUB": 63.172,
        "SEK": 8.5748,
        "SGD": 1.3624,
        "THB": 34.65,
        "TRY": 2.9998,
        "ZAR": 13.79,
        "EUR": 0.89119
      }
  }
  ```


