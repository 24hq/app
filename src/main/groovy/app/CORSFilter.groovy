package app

import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CORSFilter extends OncePerRequestFilter {

    @Override
    void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        def header = response.&setHeader

        header "Access-Control-Allow-Origin", "*"
        header "Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE"
        header "Access-Control-Max-Age", "3600"
        header "Access-Control-Allow-Headers", "x-requested-with"

        filterChain.doFilter request, response
    }
}