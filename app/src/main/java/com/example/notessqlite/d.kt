package com.example.notessqlite// main.kt

import com.example.notessqlite.email.Email
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.lang.Exception

fun emailEngine(email: Email) {
    // Replace with your actual SMTP server credentials and host information.
    // DANGER: Never hard-code sensitive credentials in a real application! Use a config file or environment variables.
    val username = "williamsian845@gmail.com"
    val password = "mznu fxge mwsr udpp" // Use an app-specific password if 2-factor auth is enabled
    val host = "smtp.gmail.com"
    val port = "465" // Common ports are 587 (TLS) or 465 (SSL)

    // Set up the SMTP server properties.
    val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", host)
        put("mail.smtp.port", port)
    }

    // Create a new session with the authenticator.
    val session = Session.getInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(username, password)
        }
    })

    try {
        // Create a new MimeMessage object.
        val message = MimeMessage(session).apply {
            setFrom(InternetAddress(email.from))
            addRecipient(Message.RecipientType.TO, InternetAddress(email.to))
            subject = email.subject
            setText(email.body)
        }

        // Send the message.
        Transport.send(message)
        println("Email sent successfully to ${email.to}")
    } catch (e: Exception) {
        println("Failed to send email. Error: ${e.message}")
        e.printStackTrace()
    }
}

fun scheduleEmail(email: Email, delay: Long, unit: TimeUnit) {
    // Create a single-threaded scheduled executor service.
    val scheduler = Executors.newSingleThreadScheduledExecutor()

    // Define the task to be executed.
    val emailTask = Runnable {
        emailEngine(email)
    }

    println("Scheduling email to be sent in $delay ${unit.name.lowercase()}...")

    // Schedule the task to run once after the specified delay.
    scheduler.schedule(emailTask, delay, unit)

    // Important: Shut down the scheduler after the task is submitted.
    // In a real application, you might want to keep the scheduler running.
    scheduler.shutdown()
}

fun main() {
    // Define the email details.
    val scheduledEmail = Email(
        to = "ianmwendwa91e@gmail.com", // <-- CHANGE THIS
        from = "williamsian845@gmail.com", // <-- CHANGE THIS
        subject = "This is a Scheduled Email",
        body = "This email was sent using a Kotlin scheduler!"
    )

    // Schedule the email to be sent after 10 seconds.
    scheduleEmail(scheduledEmail, 10, TimeUnit.SECONDS)
}
