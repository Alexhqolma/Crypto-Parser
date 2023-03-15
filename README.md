# Crypto-Parser App

## <img alt="" src="https://img.icons8.com/external-photo3ideastudio-lineal-color-photo3ideastudio/452/external-description-museum-photo3ideastudio-lineal-color-photo3ideastudio.png" width="50" style="margin-bottom:-18px"> Description
This project is about parser of cryptocurrency.
System update 3 currencies every 10 sec.

## <img alt="" src="https://img.icons8.com/external-flaticons-flat-flat-icons/452/external-functionality-no-code-flaticons-flat-flat-icons.png" width="50" style="margin-bottom:-15px"> Available functionality
- Get min price of any currency
- Get max price of any currency
- Create csv file of currency statistic

## <img alt="" src="https://img.icons8.com/office/452/parallel-tasks.png" width="50" style="margin-bottom:-15px"> Project structure
- 3-Tier Architecture
- Controller
- Service
- Model
  - DTO

<img alt="" src="https://image.shutterstock.com/image-vector/get-started-icon-internet-button-600w-265614941.jpg" width="50" style="margin-bottom:-17px"> Steps required to get started
- 
- Clone repository
- Run project
- You can review 3 currencies: BTC, ETH, XRP
- /cryptocurrencies - You will receive a list of all currencies
- /cryptocurrencies/minprice?name=BTC - You will receive min price for selected currency
- /cryptocurrencies/maxprice?name=BTC - You will receive max price for selected currency
- /cryptocurrencies/csv - Will generate *.csv file with statistic of min and max prices for all 3 currencies