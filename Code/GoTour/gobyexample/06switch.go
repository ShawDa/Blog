package main

import (
	"fmt"
	"reflect"
	"time"
)

func main() {
	i := 2
	fmt.Print("Write ", i, " as ")
	switch i {
	case 1:
		fmt.Println("one")
	case 2:
		fmt.Println("two")
	case 3:
		fmt.Println("three")
	}

	switch time.Now().Weekday() {
	case time.Saturday, time.Sunday:
		fmt.Println("It's the weekend")
	default:
		fmt.Println("It's a weekday")
	}

	t := time.Now()
	switch {
	case t.Hour() < 12:
		fmt.Println("It's before noon")
	default:
		fmt.Println("It's after noon")
	}

	num := 3
	switch {
	case num <= 1:
		fmt.Println("num <= 1")
	case num >= 2:
		fmt.Println("num >= 2")
		fallthrough
	case num >= 3:
		fmt.Println("num >= 3")
	default:
		fmt.Println("No right condition")
	}

	whatAmI := func(i interface{}) {
		switch t := i.(type) {
		case bool:
			fmt.Println("I'm a bool")
		case int:
			fmt.Println("I'm a int")
		default:
			fmt.Printf("Don't know type %s\n", reflect.TypeOf(t))
		}
	}
	whatAmI(true)
	whatAmI(1)
	whatAmI("hello")
}

/*
Write 2 as two
It's a weekday
It's before noon
num >= 2
num >= 3
I'm a bool
I'm a int
Don't know type string
*/
