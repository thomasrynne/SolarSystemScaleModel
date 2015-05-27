#Solar System Scale Model

This is an small experimental project to create a functional UI using [scalajs](http://www.scala-js.org/) and [Mithril](https://lhorie.github.io/mithril/).

It wraps pure Scala classes which represent a scale model of the solar system but rather than using a mutable 'solar system model' with listeners to update the DOM when values change the model is immutable and the view representation of the model is re-generated after every change.

Rather than actually replacing the DOM every time a change is made (which is slow, causes flicker and breaks focus)
Mithril is used to generate a virtual DOM and only apply the changes to the real browser DOM.

A new [scalatags](https://github.com/lihaoyi/scalatags) backend is use for the html generation.

This approach does seem particularly suited to Scala and would be especially useful if there are many parts of the page to keep in sync.