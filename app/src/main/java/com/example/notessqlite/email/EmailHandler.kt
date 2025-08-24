package com.example.notessqlite.email

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.time.LocalDate
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailHandler {

    private fun emailEngine(email: Email){
        val username = "williamsian845@gmail.com"
        val password = "mznu fxge mwsr udpp"
        val smtpHost = "smtp.gmail.com"
        val smtpPort = "465"

        val serverProps = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", smtpHost)
            put("mail.smtp.port", smtpPort)
        }

        val session = Session.getInstance(serverProps, object : Authenticator(){
            override fun getPasswordAuthentication(): PasswordAuthentication? {
                return PasswordAuthentication(username, password)
            }
        })

        val message = MimeMessage(session).apply {
            setFrom(InternetAddress(email.from))
            addRecipient(Message.RecipientType.TO, InternetAddress(email.to))
            subject = email.subject
            setText(email.body)
        }
        Transport.send(message)
    }

    fun sendEmail(subject: String, body: String){
        val email = Email(
            to = "ianmwendwa91e@gmail.com",
            from = "williamsian845@gmail.com",
            subject = subject,
            body = body
        )
        emailEngine(email)
    }

    data class Amies(val name: String, val dob: LocalDate)
    fun birthdayWatcher(){
        val amiesInfo = listOf(
            Amies("Test subject", LocalDate.of(2025, 8, 25))
        )

        val sth = mutableListOf<Amies>()
        sth.add(Amies("", LocalDate.now()))

        val currentDate = LocalDate.now()

        for (amie in amiesInfo){
            if (currentDate == amie.dob){
                sendEmail("It's ${amie.name}'s birthday today!", "If you are seeing this, it means the" +
                        "email was successfully sent to you!")
            }
        }
    }
    class EmailWorker(context: Context, workerParams: WorkerParameters): Worker(context, workerParams){
        override fun doWork(): Result {
            EmailHandler().birthdayWatcher()
            return Result.success()
        }
    }
}