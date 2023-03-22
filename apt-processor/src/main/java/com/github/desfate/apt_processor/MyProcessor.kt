package com.github.desfate.apt_processor

import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

class MyProcessor: AbstractProcessor() {
    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        println("Apt test")
    }

    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        System.out.println("Apt test 2")
        return false
    }
}