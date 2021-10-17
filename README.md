# Auction Service

REST API for selling and buying in auctions

### Technologies
* Spring boot
* Spring Security
* MySql

### Usage
You can access this application on address [localhost:8080](http://localhost:8080)

### Features
* Creating Account
* Making Auction
* having money in web wallet
* easy bid auctions

### API
````
- GET    /auction - list of all auction
- PUT    /auction - create new auction
- GET    /auction/id - get auction
- PATH   /auction/id - bid auction
- DELETE /auction/id - delete auction

- GET    /user - get account detail
- PUT    /user - create new user
- DELETE /user - delete user
- GET    /user/MyAuction - get owned auction
- GET    /user/userAuction/username - get list of auctions from user
- GET    /user/username - get user details
- PATH   /user/username - update user details

- GET    /wallet - get owned wallet details
- PATH   /wallet - update wallet details
````