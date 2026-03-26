# CSRF Implementation in Spring Boot & React

## How CSRF Work?

Cross-Site Request Forgery (CSRF) is a type of attack where a malicious website tricks a user's browser into making an
unwanted request to a different site where the user is authenticated. The attacker can perform actions on behalf of the
user without their consent, such as changing account settings, making purchases, or even deleting data.
It works because browser automatically sends credentials (JWT in cookie) with every request to the target site, so if
the user is logged in, the attacker's request will be authenticated and processed by the server.

## Implementation

To implement CSRF protection in a Spring Boot and React application, we can use the following approach:

1. Enable CSRF protection in Spring Boot.
    ```java
    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/v1/auth/**")
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
        }
    }
   ```
2. Add an endpoint in Spring Boot to provide the CSRF token to the React frontend.
    ```java
    @GetMapping("/csrf")
    public ResponseEntity<Void> csrf() {
    return ResponseEntity.ok().build(); 
    }
    ```
3. React frontend to fetch the CSRF token and include it in the headers of subsequent requests.
    ```javascript
    // Read CSRF token
    function getCsrfToken() {
    return document.cookie
    .split("; ")
    .find(row => row.startsWith("XSRF-TOKEN="))
    ?.split("=")[1];
    }
    
   
    api.interceptors.request.use((config) => {
    const csrfToken = getCsrfToken();
    if (csrfToken) {
    config.headers["X-XSRF-TOKEN"] = csrfToken;
    }
    return config;
    });
    ```

4. Get the CSRF token when React starts up to ensure it's available for all requests.
    ```javascript
    import React, { useEffect } from "react";
    import api from "./api/client";

    function App() {
        useEffect(() => {
        api.get("/api/csrf").catch(console.error);
    }, []);
    
    
    return (
    //skip
    );
    }

    export default App;
   ```

5. CORS need to allow header
    ```java
    config.addAllowedHeader("*"); // or add Content-Type, X-XSRF-TOKEN to the list
   ```

## Postman

To test the CSRF protection using Postman, you can follow these steps:

1. Make a GET request to the `/csrf` endpoint to retrieve the CSRF token.
2. Extract the CSRF token from the response cookies.
3. Include the CSRF token in the headers of subsequent requests (e.g. X-XSRF-TOKEN: abc123)