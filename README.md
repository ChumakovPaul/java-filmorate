# java-filmorate
Template repository for Filmorate project.

![bd_schema.png](bd_schema.png)

### Table "users"
```declarative
id             // bigint, Primary Key, autoincrement
email          // varchar, not null
login          // varchar, not null
name           // varchar
birthday       // date, not null
```
### Table "friends"
```declarative
id             // bigint, Primary Key, autoincrement
user_id        // bigint, not null, Foreign Key -> users.id
friend_id      // bigint, not null, Foreign Key -> users.id
```
### Table "films"
```declarative
film_id        // bigint, Primary Key, autoincrement
name           // varchar, not null
description    // varchar(200), not null
release_date   // date, not null
duration       // bigint, not null
mpa_id         // bigint, not null, Foreign Key -> mpa.mpa_id
```
### Table "likes"
```declarative
id             // bigint, Primary Key, autoincrement
user_id        // bigint, Foreign Key -> users.id
film_id        // bigint, Foreign Key -> films.film_id

```
### Table "genre"
```declarative
genre_id       // bigint, Primary Key, autoincrement
name           // varchar, not null
```
### Table "film_genres"
```declarative
film_id        // bigint, Foreign Key -> films.film_id
genre_id       // bigint, Foreign Key -> genres.genre_id
```
### Table "mpa"
```declarative
mpa_id         // bigint, Primary Key, autoincrement
name           // varchar, not null
```
