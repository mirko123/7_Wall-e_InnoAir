# InnoAirChallenge

[Начални идеи](https://docs.google.com/document/d/1l-GFWg-SNZemvM5Am4N7jBjvi0_tiBtCHO_s5OkabE4/edit?usp=sharing)

## Финална Идея

Мобилно приложение, което "зарежда" карта за градски транспорт директно от телефона, без да е нужно посещение на офис на ЦГМ.
Демонстира се как би може да се избегне нуждата от разнасянето на пластмасовата карта, като нейната функционалност е заменена изцяло от мобилен телефон, на същия принцип по-който се заменят дебитните карти с мобилните телефони. Също така се демонстира как текущите устройства на контрольорите биха могли да се заменят от евтин мобилен телефон.
Към приложението влиза и функционалност за закопуване на билетче за време, съответно до 30 мин, до час и до 2 часа. Което се реализира по подобен начин както система MPASS - генериране на QR код, в който се сдържа информация за момента на копуване и валидната продължителност.
Твърдя, че всички функции на приложението биха се внедрили в ЦГМ без да е нужно нищо повече от малък софтуерен ъпдейт на текущите системи и също те не отстъпват по сигурност на текущите методи.


## Сигурност
Няма значение какъв стандарт карта се използва, ако информацията върху нея е криптирана и ключовете са известни само на ЦГМ.
Комуникацията, която се осъществява между четец и телефон в приложението е [криптирана](https://github.com/mirko123/7_Wall-e_InnoAir/blob/38f845060ba21e6450c4841505df610844319afc/CardReader/app/src/main/java/com/example/android/cardreader/LoyaltyCardReader.java#L142). [Експлоитите](https://smartlockpicking.com/slides/HiP19_Cracking_Mifare_Classic_on_the_cheap_workshop.pdf) на Mifare Classic картите са известни от много време. [Ключовете](https://github.com/mirko123/7_Wall-e_InnoAir/blob/master/MainApp/app/src/main/java/com/example/mainapp/write/CardData.java) за четене и писане на всяка Mifare Classic(текущия стандарт на картите на градския транспорт) биха се получили за няколкото минути и след това да се прилагат за всяка карта за градски транспорт. 




[Презентация(pdf)](https://drive.google.com/file/d/1rWH0F7-hXBexoJJOJiqqx5m1-GbF3IM0/view?usp=sharing)

[Демо с основното приложение(видео)](https://drive.google.com/file/d/1cwmRkIEwOHvxfVxQwNtAtCIX9t2r9Z-F/view?usp=sharing)

[Демонстрация на метростанция(видео)](https://drive.google.com/file/d/1d1uFXCEHp0vATFxWwzP0fA3nAcTES76M/view?usp=sharing)

[Демо с четец на карта(видео)](https://drive.google.com/file/d/1XKwBlmZtYQhjNGFOyeyYI9yFCPX-a2hS/view?usp=sharing)

### Изглед от приложението, което би заместило текущите устройства на контрольорите.
![Валидна дата](https://github.com/mirko123/7_Wall-e_InnoAir/blob/master/Demos/valid.jpg)
![Невалидна дата](https://github.com/mirko123/7_Wall-e_InnoAir/blob/master/Demos/invalid_data.jpg)
![Невалидна карта](https://github.com/mirko123/7_Wall-e_InnoAir/blob/master/Demos/invalid_card.jpg)

### Основно приложение
![Основно меню](https://github.com/mirko123/7_Wall-e_InnoAir/blob/master/Demos/mainMenu.jpg)
![Зареждане на карта](https://github.com/mirko123/7_Wall-e_InnoAir/blob/master/Demos/zarejdane.jpg)

![Изглед покупва на билет](https://github.com/mirko123/7_Wall-e_InnoAir/blob/master/Demos/bilet1.jpg)
![Изглед на билет](https://github.com/mirko123/7_Wall-e_InnoAir/blob/master/Demos/bilet2.jpg)

![Емулиране](https://github.com/mirko123/7_Wall-e_InnoAir/blob/master/Demos/emulation.jpg)
![Visa](https://github.com/mirko123/7_Wall-e_InnoAir/blob/master/Demos/visa.jpg)

## Чужди проекти, чиито код би могъл да се намери в приложението.
[Ref. Mifare Classic Tool](https://github.com/ikarus23/MifareClassicTool)
[Ref. Card Reader sample](https://github.com/googlearchive/android-CardReader)
[Ref. Debit Card Form](https://github.com/braintree/android-card-form)

