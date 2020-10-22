package main

import "fmt"

func main() {
	fmt.Println(fib(20)) // 6765
}

func fib(n int) int {
	if n == 0 {
		return 0
	}
	if n == 1 {
		return 1
	}
	return fib(n-2) + fib(n-1)
}
