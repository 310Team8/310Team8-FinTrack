  <!-- PROJECT SHIELDS -->

[![GitHub forks](https://img.shields.io/github/forks/310Team8/FinTrack.svg?style=for-the-badge)](https://github.com/310Team8/FinTrack/network/members)
[![GitHub issues](https://img.shields.io/github/issues/310Team8/FinTrack.svg?style=for-the-badge)](https://github.com/310Team8/FinTrack/issues)
[![GitHub contributors](https://img.shields.io/github/contributors/310Team8/FinTrack.svg?style=for-the-badge)](https://github.com/310Team8/FinTrack/graphs/contributors)
[![Contributor Guidelines](https://img.shields.io/badge/Contributor-Guidelines-blue.svg?style=for-the-badge)](./CONTRIBUTING.md)
[![Code of Conduct](https://img.shields.io/badge/Code%20of%20Conduct-2.1-4baaaa.svg?style=for-the-badge)](./CODE_OF_CONDUCT.md)
[![MIT License](https://img.shields.io/github/license/310Team8/FinTrack.svg?style=for-the-badge)](https://github.com/310Team8/FinTrack/blob/main/LICENSE)
[![Wiki](https://img.shields.io/badge/Wiki-Documentation-brightgreen.svg?style=for-the-badge)](https://github.com/310Team8/FinTrack/wiki)

<br />
<div align="center">
  <h3 align="center">💰FinTrack📊</h3>
  <h3>v1.5.3</h3>

  <p align="center">
    Budgeting and Financial Management Tool
    <br />
    <br />
    <br />
 <a href="https://github.com/310Team8/FinTrack/issues/new?labels=bug&template=bug_report.md">Report Bug</a>
·
<a href="https://github.com/310Team8/FinTrack/issues/new?labels=enhancement&template=feature_request.md">Request Feature</a>
·
<a href="https://github.com/310Team8/FinTrack/issues/new?labels=documentation&template=documentation_request.md">Request Documention</a>

</div>
<br />

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#project-insight">Project Insight</a></li>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#what-does-this-project-do">What does this project do?</a></li>
        <li><a href="#why-is-this-project-useful">Why is this project useful?</a></li>
        <li><a href="#tech-stack-used">Tech Stack Used</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#versions">Versions</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
     <li><a href="#contacts">Contacts</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

## Project Insight

![Alt](https://repobeats.axiom.co/api/embed/1ab332dbfcc40cb69c256641d05a9e80090a5b3e.svg "Repobeats analytics image")

<!-- ABOUT THE PROJECT -->

## About The Project 🚀

FinTrack is designed to be a comprehensive financial management tool aimed at helping users gain better control over their finances. Whether for personal use or business purposes, FinTrack provides a robust platform for tracking income, expenses, and budgets. The tool is built with a focus on user-friendly data visualisation and forecasting features, ensuring users can easily interpret their financial data and make informed decisions. You can read more at [Project Description](./DESCRIPTION.md) about the next steps!

This project has been deployed to Render, a free hosting service that provides automated builds and deployments. The application is configured to automatically rebuild and redeploy whenever there is a merge to the main branch. This ensures that the latest changes are always reflected in the live environment without any manual intervention.

Make sure to contact us at <a href="#contacts">Contacts</a> for the database variables to store in your environment variables.

### What does this project do? 🤔

FinTrack empowers users to efficiently manage their financial activities. It enables users to create and maintain personal or business financial profiles, categorize and monitor expenses, set and adhere to budgets, and forecast both income and asset values. Additionally, FinTrack offers goal-setting and tracking features, allowing users to achieve their financial objectives with ease.

### Why is this project useful? 🌟

FinTrack is useful because it provides a centralized platform for managing and optimizing financial resources, making it easier for individuals and businesses to track their financial health. By offering features such as expense categorization, budget monitoring, and income forecasting, it helps users make informed financial decisions, avoid overspending, and achieve their financial goals. The data visualization tools further enhance the user experience by presenting complex financial data in an easy-to-understand format, making financial management more accessible and efficient.

### Tech Stack Used 💻

- [![Vaadin](https://img.shields.io/badge/Vaadin-00B4F0.svg?style=for-the-badge&logo=vaadin&logoColor=white)](https://vaadin.com/)
- [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F.svg?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
- [![CSS](https://img.shields.io/badge/CSS-1572B6.svg?style=for-the-badge&logo=css3&logoColor=white)](https://developer.mozilla.org/en-US/docs/Web/CSS)
- [![Java](https://img.shields.io/badge/Java-007396.svg?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)

<!-- GETTING STARTED -->

## Getting Started⚙️

### Prerequisites📋

You will need the following software installed:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Git](https://git-scm.com/downloads)
- [Maven](https://maven.apache.org/download.cgi)

To set up Maven:

- [Set up Maven Environment for Windows](https://www.qamadness.com/knowledge-base/how-to-install-maven-and-configure-environment-variables/)
- [Set up Maven Environment for Mac/Linux](https://www.baeldung.com/install-maven-on-windows-linux-mac)

### Installation⚡

1. **Fork the Repo**:
   Click the "Fork" button at the top right of the repository page to fork the project to your GitHub account.

2. **Clone the Forked Repo using Terminal/Command Prompt**:
   Replace `your-username` with your GitHub username.

   ```sh
   git clone https://github.com/your-username/FinTrack.git
   ```

3. **Navigate to the Project Directory**:

   ```sh
   cd FinTrack
   ```

4. **Add .env file to Root Directory**:

   Before running the application locally, make sure to set create a .env file in the root directory. The env file should contain the database URL, username, and password.

   Please contact us at <a href="#contacts">Contacts</a> and ask a second half author for the details required in the .env file.

5. **Install Maven Dependencies**:

   ```sh
   mvn clean install
   ```

6. **Run the Project**:
   ```sh
   mvn clean spring-boot:run
   ```
   Alternatively, you can open Application.java in your IDE (such as IntelliJ IDEA or VS Code) and press the "Run" button to start the application.
   Once the application is running, your web browser should open automatically. If not, open a new browser window and type http://localhost:8080 to access the application.

## Versions📝

- [Fintrack v1.0.0](https://github.com/310Team8/FinTrack/releases/tag/v1.0.0)
- [Fintrack v1.5.3](https://github.com/310Team8/FinTrack/releases/tag/v1.5.3) (Latest)

<!-- CONTRIBUTING -->

## Contributing🤝

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please read more at [Contributor Guidelines](./CONTRIBUTING.md)

<!-- LICENSE -->

## License📜

Distributed under the MIT License. Read more at [MIT License](./LICENSE) for more information.

<!-- CONTACTS -->

## Contacts📞

If you have any inquiries related to this project or face any issues contact us using the information below:

### First half authours:

- Email: hshi270@aucklanduni.ac.nz
- Email: sdes755@aucklanduni.ac.nz
- Email: sbha878@aucklanduni.ac.nz
- Email: srab017@aucklanduni.ac.nz
- Email: jman988@aucklanduni.ac.nz

### Second half authors:

- [Aaron Worsnop](https://github.com/aaronworsnop) Email: awor776@aucklanduni.ac.nz
- [Chris Kwon](https://github.com/hyukjun3) Email: ckwo873@aucklanduni.ac.nz
- [Huaiwen Zhang](https://github.com/Deagle0422) Email: hzha919@aucklanduni.ac.nz
- [Ming Huang](https://github.com/minghan36) Email: mhua948@aucklanduni.ac.nz
- [Troy Mackenzie-Smee](https://github.com/tmacsmee) Email: tmac847@aucklanduni.ac.nz
- [Venxia Niu](https://github.com/vniu740) Email: vniu740@aucklanduni.ac.nz

<!-- ACKNOWLEDGMENTS -->

## Acknowledgments

- [Code of Conduct Template](https://www.contributor-covenant.org/)
- [Repository Insight Template](https://repobeats.axiom.co/)
- [README Template](https://github.com/othneildrew/Best-README-Template?tab=readme-ov-file)
- [Image Shields](https://shields.io)
