package main

import (
	"fmt"
	"time"
)

func main() {
	f("direct")

	go f("go")

	go func(msg string) {
		fmt.Println(msg)
	}("going")

	go f("routine")

	time.Sleep(time.Second)
	fmt.Println("done")
}

/*
direct : 0
direct : 1
direct : 2
routine : 0
routine : 1
routine : 2
go : 0
go : 1
go : 2
going
done
*/
func f(from string) {
	for i := 0; i < 3; i++ {
		fmt.Println(from, ":", i)
	}
}
