package main

import (
	"fmt"
	"strconv"
	"sync"
	"time"
)

var (
	myMap = make(map[int]int, 10)
	lock  sync.Mutex
)

func main() {
	go test() // goroutine

	for i := 0; i <= 5; i++ {
		fmt.Println("main" + strconv.Itoa(i) + ":" + time.Now().String())
		time.Sleep(time.Second)
	}

	for i := 0; i < 100; i++ {
		go add(i)
	}

	lock.Lock()
	fmt.Printf("%v %v\n", time.Now().String(), myMap)
	lock.Unlock()
}

func test() {
	for i := 0; i < 5; i++ {
		fmt.Println("test" + strconv.Itoa(i) + ":" + time.Now().String())
		time.Sleep(time.Second)
	}
}

func add(n int) {
	res := 0
	for i := 1; i <= n; i++ {
		res += i
	}

	lock.Lock()
	myMap[n] = res
	fmt.Println(time.Now().String() + " " + strconv.Itoa(n) + ":" + strconv.Itoa(res))
	lock.Unlock()
}
