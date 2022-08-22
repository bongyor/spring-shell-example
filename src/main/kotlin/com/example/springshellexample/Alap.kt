package com.example.springshellexample

import org.jline.utils.AttributedStringBuilder
import org.jline.utils.AttributedStyle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.component.flow.ComponentFlow
import org.springframework.shell.component.flow.SelectItem
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import org.springframework.util.StringUtils
import javax.validation.constraints.Min
import javax.validation.constraints.PositiveOrZero


@ShellComponent
class Alap {
    @Autowired
    private lateinit var componentFlowBuilder: ComponentFlow.Builder

    @ShellMethod("Két szám összeadása")
    fun osszead(egyik: Int, masik: Int) = egyik + masik

    @ShellMethod("Két vagy három szám összeadása")
    fun osszead3(
        @PositiveOrZero egyik: Int,
        @Min(10) masik: Int,
        @ShellOption(defaultValue = "100", help = "Alapból 100") harmadik: Int
    ) = egyik + masik + harmadik

    @ShellMethod("Flow futtatása")
    fun runFlow() {
        val single1SelectItems: MutableMap<String, String> = HashMap()
        single1SelectItems["key1"] = "value1"
        single1SelectItems["key2"] = "value2"
        val multi1SelectItems: List<SelectItem> = listOf(
            SelectItem.of("key1", "value1"),
            SelectItem.of("key2", "value2"),
            SelectItem.of("key3", "value3")
        )
        val flow: ComponentFlow = componentFlowBuilder.clone().reset()
            .withStringInput("field1")
            .renderer{
                val builder = AttributedStringBuilder()
                builder.append(it.name)
                builder.append(" ")
                if (it.resultValue != null) {
                    builder.append(it.resultValue)
                } else {
                    val input: String? = it.input
                    if (StringUtils.hasText(input)) {
                        builder.styled(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN),input)
                    } else {
                        builder.styled(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED),"[Default " + it.defaultValue + "]")
                    }
                }
                return@renderer listOf(builder.toAttributedString())

            }
            .name("Field1")
            .defaultValue("defaultField1Value")
            .postHandler { println("Field 1 post handler ${it.resultValue}") }
            .preHandler { println("Field 1 pre handler ${it.resultValue}") }
            .and()
            .withStringInput("field2")
            .name("Field2")
            .and()
            .withConfirmationInput("confirmation1")
            .name("Confirmation1")
            .postHandler { println("Confirmation post handler ${it.resultValue}") }
            .preHandler { println("Confirmation pre handler ${it.resultValue}") }
            .and()
            .withPathInput("path1")
            .name("Path1")
            .postHandler { println("Path1 post handler ${it.resultValue}") }
            .preHandler { println("Path1 pre handler ${it.resultValue}") }
            .and()
            .withSingleItemSelector("single1")
            .name("Single1")
            .selectItems(single1SelectItems)
            .and()
            .withMultiItemSelector("multi1")
            .name("Multi1")
            .selectItems(multi1SelectItems)
            .postHandler { println("Multi1 post handler ${it.values}") }
            .preHandler { println("Multi1 pre handler ${it.values}") }
            .and()
            .build()
        val result = flow.run()
        println(result)
    }
}