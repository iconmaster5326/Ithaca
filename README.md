# Ithaca
**Ithaca Lisp** (Usually just **Ithaca** for short) is a new, radical dialect of Lisp/Scheme that aims to improve the language through fundamental alterations in syntax and style. It most heavily borrows from Scheme (Ithaca is simple, single-namespaced, etc) but is not a direct dialect of it.

This repository contains a reference implementation of Ithaca Lisp, as well as documents detailing the Ithaca language standard.

## Why Ithaca?

It's where I live.

## Why Make Ithaca?

Lisp has many desirable and/or interesting properties for a programming language... But it's old. It's one of the oldest languages out there. It's developed a layer of crust that impedes normal programming in the language. Ithaca Lisp throws off the shackles of the ancients, allowing you to write programs quickly and readably.

## What Changes Are We Talking About?

Here's a short list:
* Many functions have been renamed so their names actually make sense. `lambda` is `func`, `car` is `head`, `cdr` is `behead`, just to name a few.
* There is only one `let`, which works like the best of all `let`s: Names are bound, then values are evaluated and given to the names in sequence.
* Multiple returns are gone! Why? Because when you want to obtain multiple values from a function, it's often a pain to do so. Just return a list instead. Plus, it lets us simplify the syntax of stuff like `let` in neat ways.
* Generic functions are the norm! At one time, you had many functions to do one thing- If you want to find the length of something, there's `length`, `vector-length`, `string-length`... The list goes on. Now, `length` just plain works on all those data types.
* User-defined data types are first-class citizens of Ithaca. No longer are the days of struggling to make sure your custom data types print, hash, or test for equality right.

## Examples?

Yes.

```lisp
;; Here we define a functional stream - Essentially, A list that generates values on command.
(define/type <stream>)

(define (stream? x)
  (is? x <stream>)
)

(define/macro (stream . body)
  `(new <stream> (func () ,@body))
)

(define head (let (super head) (func (x)
  (if (stream? x)
    (head ((get-value x)))
    (super x)
  )
)

(define behead (let (super head) (func (x)
  (if (stream? x)
    (behead ((get-value x)))
    (super x)
  )
)

(define (print-stream s)
  (while s
    (let ((val next) ((get-value x)))
      (print val)
      (set! s next)
    )
  )
)

; prints 0 then 1 then 2
(print-stream (stream (pair 0 (stream (pair 1 (stream (pair 2 #f)))))))

;; Here we define a promise - essentially, a cachable value.
(define/type <promise>)

(define/macro (delay . body)
  (let (forced? (symbol) value (symbol))
  `(let (,forced? #f ,value #v)
     (new <promise> (func ()
        (unless ,forced?
          (set! ,forced? #t)
          (set! ,value (begin @,body))
        )
        ,value
     ))
    )
  )
)

(define (force p)
  ((get-value p))
)

; prints Hello, World only once.
(define hello (delay (print "Hello, World!") #v))
(force hello)
(force hello)
(force hello)
```

