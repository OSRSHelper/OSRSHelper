<div align="center">

# OSRS Helper
An open source Android application for various Old School RuneScape tooling since 2014

![Apache 2.0](https://img.shields.io/github/license/OSRSHelper/OSRSHelper) ![https://discord.gg/DRQuGaH](https://img.shields.io/discord/730211713462566932)
  
</div>

OSRS Helper is an application aiming at enhancing player access to tools while on their Android device. This project includes an Android application and a back-end Python Flask API.

[💬 Join us on discord!](https://discord.gg/DRQuGaH)

### Features

- [Hiscore lookup](#hiscore-lookup)
- [Experience gain tracking](#experience-tracking)
- [Experience gain inspection](#experience-tracking)
- [Experience gain leaderboards](#experience-tracking)
- [Grand Exchange lookup](#grand-exchange)
- [Widgets for hiscores and Grand Exchange](#grand-echange)
- [World map with points of interests](#world-map)
- [OSRS News with push notifications](#osrs-news)

#### Hiscore Lookup
Hiscore lookups are perfomed through the [Wise Old Man (WOM)](https://github.com/wise-old-man/wise-old-man) project to ensure persistent experience caching and faster load times. When requesting a user that is known to WOM, their latest cached version is displayed. The user can then issue an update request to obtain fresh data from Jagex's Hiscore API. 

Offline caching is also implemented, allowing users to lookup previously fetched data on their devices without internet access.

#### Experience Tracking
The WOM project is also used to track experience gain, with the OSRSHelper application acting as native front-end. Currently implemented feature include obtaining a player's experience gain for specific durations, inspecting deltas between all WOM snapshots, and a leaderboard for the current top players.

#### Grand Exchange
An item search is available, which displays various information about an items' current and past prices. Graphs are displayed with the fluctuation of prices and the price trend. A widget is also available to display an item's price on the home screen.

#### World map
A complete world map with various points of interest is available, with various points of interests added as shortlinks.

#### OSRS News
The latest OSRS news are browsable from within the application. A toggle can also be enabled which will send a push notification to the mobile device when news are published by either the Jagex team or the OSRSHelper team.
