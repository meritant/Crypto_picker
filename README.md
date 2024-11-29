# Crypto Picker

## Overview
Crypto Picker is a cryptocurrency tracking application designed to provide users with real-time crypto market insights, price tracking, and personalized notifications.

## Key Features
- Select and track major cryptocurrencies
- Real-time cryptocurrency price tracking
- Price movement alerts (account required)
- Progressive Web App (PWA) with desktop/mobile shortcuts
- Persistent user settings storage
- Daily email market reports (account-based)

## Technology Stack

### Backend
- **Framework**: Spring Boot
- **Authentication**: Spring Security
- **Database Interaction**: Spring Data JPA
- **Authentication Tokens**: Java JWT (jjwt)

### Database
- **Database**: PostgreSQL
- **Storage Purposes**:
  - User account management
  - User preferences and settings
  - Cryptocurrency price caching
  - Alert configurations

### API Integration
- **Cryptocurrency Data Source**: CoinGecko API
- **API Communication**: Spring WebClient/RestTemplate

### Frontend
- **Framework**: React.js
- **Notifications**: Web Push API
- **Local Storage**: Browser LocalStorage API

### Additional Libraries
- **Code Optimization**: Lombok
- **Task Scheduling**: Quartz Scheduler
- **Utilities**: Apache Commons
- **Testing**: JUnit, Mockito

## Getting Started
(Detailed setup instructions to be added)

## Contributing
(Contribution guidelines to be added)

## License
(License information to be added)