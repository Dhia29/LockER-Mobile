package com.example.lockermobile.data.local.seeder

import com.example.lockermobile.data.local.dao.*
import com.example.lockermobile.data.local.entity.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseSeeder @Inject constructor(
    private val jobDao: JobDao,
    private val postDao: PostDao,
    private val companyDao: CompanyDao
) {
    suspend fun seed() {
        seedCompanies()
        seedJobs()
        seedPosts()
    }

    private suspend fun seedCompanies() {
        val companies = listOf(
            CompanyEntity("1", "Google", "", "Work on the world's most popular search engine and more.", "Mountain View, CA", "Technology"),
            CompanyEntity("2", "Meta", "", "Connect the world through social media.", "Menlo Park, CA", "Social Media"),
            CompanyEntity("3", "Gojek", "", "On-demand multi-service platform in Indonesia.", "Jakarta", "Technology"),
            CompanyEntity("4", "Traveloka", "", "Leading travel and lifestyle platform.", "Jakarta", "Travel"),
            CompanyEntity("5", "Shopee", "", "Leading e-commerce platform in SE Asia.", "Singapore", "E-commerce")
        )
        companies.forEach { companyDao.insertCompany(it) }
    }

    private suspend fun seedJobs() {
        val jobTitles = listOf("Android Developer", "Backend Engineer", "Product Designer", "Data Scientist", "Project Manager")
        val companies = listOf("Google", "Meta", "Gojek", "Traveloka", "Shopee")
        val categories = listOf("Software Development", "Design", "Management", "Data Science")
        val types = listOf("Full-time", "Remote", "Part-time", "Contract")

        val jobs = mutableListOf<JobEntity>()
        for (i in 1..25) {
            jobs.add(
                JobEntity(
                    id = i.toString(),
                    companyId = (i % 5 + 1).toString(),
                    title = "${jobTitles[i % 5]} ${if (i > 5) "Level $i" else ""}",
                    companyName = companies[i % 5],
                    location = if (i % 2 == 0) "Jakarta" else "Remote",
                    salary = "Rp ${10 + i}.000.000 - ${20 + i}.000.000",
                    type = types[i % 4],
                    logoUrl = "",
                    description = "Comprehensive job description for position $i at ${companies[i % 5]}. We are looking for talented individuals to join our growing team.",
                    postedTime = "$i hours ago",
                    category = categories[i % 4]
                )
            )
        }
        jobDao.insertJobs(jobs)
    }

    private suspend fun seedPosts() {
        val posts = mutableListOf<PostEntity>()
        for (i in 1..15) {
            posts.add(
                PostEntity(
                    id = i.toString(),
                    authorName = "User $i",
                    authorAvatar = "",
                    authorRole = "Professional $i",
                    content = "This is community post number $i. Discussion about the future of work and technology trends in Indonesia. #LockER #Career #Tech",
                    timestamp = "$i days ago",
                    likesCount = i * 10,
                    commentsCount = i * 2,
                    isLiked = i % 3 == 0
                )
            )
        }
        postDao.insertPosts(posts)
    }
}
