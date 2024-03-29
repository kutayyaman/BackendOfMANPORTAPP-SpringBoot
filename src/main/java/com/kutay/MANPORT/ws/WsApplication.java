package com.kutay.MANPORT.ws;

import com.kutay.MANPORT.ws.domain.*;
import com.kutay.MANPORT.ws.dto.UserDTO;
import com.kutay.MANPORT.ws.repository.*;
import com.kutay.MANPORT.ws.service.IUserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class WsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WsApplication.class, args);
    }

    @Bean
//ModelMapper nesnesinden 1 tane olustur ve springin IoC containerina koy demis oluyoruz ve boylelikle dependency injection ile direkt alip kullanabilcez.
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); //ENUM'lari convert ederken sikinti vermemesi icn bu gerekliymis
        return modelMapper;
    }

    //, CountryRepository countryRepository, PlantRepository plantRepository, ServerRoomRepository serverRoomRepository, BackendRepository backendRepository, FrontendRepository frontendRepository, DatabaseRepository databaseRepository, TeamRepository teamRepository, ApplicationRepository applicationRepository, JobRepository jobRepository
    @Bean
    CommandLineRunner createInitialUsers(IUserService userService, IssueRepository issueRepository, CountryRepository countryRepository, ServerRepository serverRepository, ApplicationRepository applicationRepository, JobInterfaceRepository jobInterfaceRepository, JobImplementsRepository jobImplementsRepository, ApplicationServerRepository applicationServerRepository, TeamRepository teamRepository, LinkRepository linkRepository, ApplicationCountryRepository applicationCountryRepository) { //Spring projesi ayaga kalktigi zaman otomatik olarak bunun run methodu çalışır ve biz bunu başlangıç verileri eklemek için kullanıcaz.
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                if (issueRepository.countAllByRowStatus(RowStatus.ACTIVE) <= 0) {
                    createTeams();
                    createUsers();
                    createCountries();
                    createServers();
                    createApplications();
                    createJobInterfaces();
                    setupApplicationsToServers();
                    createIssues();
                }
            }

            private void createTeams() {
                List<Team> teams = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    Team team = new Team();
                    team.setName("Team" + i);
                    teams.add(team);
                }
                teamRepository.saveAll(teams);
            }


            private void setupApplicationsToServers() {
                if (jobImplementsRepository.countAllByRowStatus(RowStatus.ACTIVE) <= 0) {
                    List<JobImplement> jobImplements = new ArrayList<>();

                    List<Application> applicationList = applicationRepository.findAllByRowStatusWithInterfaces(RowStatus.ACTIVE);
                    List<Server> serverList = serverRepository.findAllByRowStatus(RowStatus.ACTIVE);
                    List<LinkEnvironmentType> linkEnvironmentTypes = Arrays.asList(LinkEnvironmentType.values());
                    List<LinkType> linkTypes = Arrays.asList(LinkType.values());
                    List<LinkSpecificType> linkSpecificTypes = Arrays.asList(LinkSpecificType.values());
                    System.out.println("BURDAAA" + applicationList.size());
                    int i = 0;
                    for (Application application : applicationList) {
                        if (application.getShortName() == "App4") {
                            int a = 5;
                        }
                        List<JobInterface> jobInterfaces = application.getJobInterfaces();
                        for (JobInterface jobInterface : jobInterfaces) {
                            JobImplement jobImplement = new JobImplement();
                            jobImplement.setJobInterface(jobInterface);
                            jobImplement.setServer(serverList.get(i));
                            jobImplements.add(jobImplement);
                        }
                        Server server = serverList.get(i);
                        ApplicationServer applicationServer = new ApplicationServer();
                        applicationServer.setApplication(application);
                        applicationServer.setServer(server);

                        Country country = server.getCountry();
                        ApplicationCountry applicationCountry = applicationCountryRepository.findFirstByApplicationAndCountryAndRowStatus(application, country, RowStatus.ACTIVE);
                        if (applicationCountry == null) {
                            applicationCountry = new ApplicationCountry();
                            applicationCountry.setApplication(application);
                            applicationCountry.setCountry(country);
                            applicationCountry.setCount(1);
                        } else {
                            applicationCountry.setCount(applicationCountry.getCount() + 1);
                        }
                        applicationCountryRepository.save(applicationCountry);

                        applicationServerRepository.save(applicationServer);
                        serverRepository.save(server);


                        List<Link> links = new ArrayList<>();
                        for (int j = 0; j < 3; j++) {
                            Link link = new Link();
                            link.setLinkEnvironmentType(linkEnvironmentTypes.get(j % 3));
                            link.setName(linkEnvironmentTypes.get(j % 3).toString() + j);
                            link.setLinkType(linkTypes.get(j % 2));
                            link.setLinkSpecificType(linkSpecificTypes.get(j % 2));
                            link.setUrl(linkEnvironmentTypes.get(j).toString() + ":8080/" + application.getShortName());
                            link.setApplicationServer(applicationServer);
                            links.add(link);
                        }
                        linkRepository.saveAll(links);

                        i++;
                        if (i > 4) {
                            i = 0;
                        }
                    }
                    //alt kisim app0'i server4'e kurmak icin
                    for (JobInterface jobInterface : applicationList.get(0).getJobInterfaces()) {
                        JobImplement jobImplement = new JobImplement();
                        jobImplement.setJobInterface(jobInterface);
                        jobImplement.setServer(serverList.get(4));
                        jobImplements.add(jobImplement);
                    }
                    Server server = serverList.get(4);
                    ApplicationServer applicationServer = new ApplicationServer();
                    applicationServer.setApplication(applicationList.get(0));
                    applicationServer.setServer(server);

                    Country country = server.getCountry();
                    ApplicationCountry applicationCountry = applicationCountryRepository.findFirstByApplicationAndCountryAndRowStatus(applicationList.get(0), country, RowStatus.ACTIVE);
                    if (applicationCountry == null) {
                        applicationCountry = new ApplicationCountry();
                        applicationCountry.setApplication(applicationList.get(0));
                        applicationCountry.setCountry(country);
                        applicationCountry.setCount(1);
                    } else {
                        applicationCountry.setCount(applicationCountry.getCount() + 1);
                    }
                    applicationCountryRepository.save(applicationCountry);


                    applicationServerRepository.save(applicationServer);
                    jobImplementsRepository.saveAll(jobImplements);

                    List<Link> links = new ArrayList<>();
                    for (int j = 0; j < 3; j++) {
                        Link link = new Link();
                        link.setLinkEnvironmentType(linkEnvironmentTypes.get(j % 3));
                        link.setName(linkEnvironmentTypes.get(j % 3).toString() + j);
                        link.setLinkType(linkTypes.get(j % 2));
                        link.setLinkSpecificType(linkSpecificTypes.get(j % 2));
                        link.setUrl(linkEnvironmentTypes.get(j).toString() + ":8080/" + applicationList.get(0).getShortName()+"benEkledim");
                        link.setApplicationServer(applicationServer);
                        links.add(link);
                    }
                    linkRepository.saveAll(links);


                    /*List<JobImplement> jobImplements = new ArrayList<>();
                    List<JobInterface> jobInterfaces = jobInterfaceRepository.findAllByRowStatus(RowStatus.ACTIVE);
                    List<Server> serverList = serverRepository.findAllByRowStatus(RowStatus.ACTIVE);
                    for (int i = 0; i < 5; i++) {
                        JobImplement jobImplement = new JobImplement();
                        jobImplement.setJobInterface(jobInterfaces.get(i));
                        jobImplement.setServer(serverList.get(i));
                        jobImplements.add(jobImplement);
                    }
                    JobImplement jobImplement = new JobImplement();
                    jobImplement.setJobInterface(jobInterfaces.get(5));
                    jobImplement.setServer(serverList.get(1));
                    jobImplements.add(jobImplement);
                    jobImplementsRepository.saveAll(jobImplements);*/
                }
            }

            private void createJobInterfaces() {
                if (jobInterfaceRepository.countAllByRowStatus(RowStatus.ACTIVE) <= 0) {
                    List<JobInterface> jobInterfaces = new ArrayList<>();
                    List<Application> applicationList = applicationRepository.findAllByRowStatus(RowStatus.ACTIVE);
                    for (int i = 0; i < 5; i++) {
                        JobInterface jobInterface = new JobInterface();
                        jobInterface.setApplication(applicationList.get(i));
                        jobInterface.setName("Job1");
                        jobInterfaces.add(jobInterface);
                    }
                    JobInterface jobInterface = new JobInterface();
                    jobInterface.setApplication(applicationList.get(1));
                    jobInterface.setName("Job2");
                    jobInterfaces.add(jobInterface);
                    jobInterfaceRepository.saveAll(jobInterfaces);
                }
            }

            private void createApplications() {
                if (applicationRepository.countAllByRowStatus(RowStatus.ACTIVE) <= 0) {
                    List<Application> applicationList = new ArrayList<>();
                    User user = userService.findByEmail("yamankutay1@gmail.com");
                    for (int i = 0; i < 5; i++) {
                        Application application = new Application();
                        application.setFullName("Application" + i);
                        application.setShortName("App" + i);
                        application.setBusinessAreaType(BusinessAreaType.Manufacturing);
                        application.setLineOfBackendCode(2400);
                        application.setLineOfFrontendCode(1600);
                        application.setUser(user);
                        applicationList.add(application);
                    }
                    applicationRepository.saveAll(applicationList);
                }
            }

            private void createServers() {
                List<Country> countryList = countryRepository.findAllByRowStatus(RowStatus.ACTIVE);
                List<Server> serverList = new ArrayList<>();
                if (serverRepository.countAllByRowStatus(RowStatus.ACTIVE) <= 0) {
                    for (int i = 0; i < 5; i++) {
                        Server server = new Server();
                        server.setCountry(countryList.get(i));
                        server.setName("Prod" + i);
                        serverList.add(server);
                    }
                    serverRepository.saveAll(serverList);
                }
            }

            private void createCountries() {
                if (countryRepository.countAllByRowStatus(RowStatus.ACTIVE) <= 0) {
                    List<Country> countryList = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        Country country = new Country();
                        country.setName("Country" + i);
                        countryList.add(country);
                    }
                    countryRepository.saveAll(countryList);
                }
            }

            private void createUsers() {
                if (userService.findByEmail("yamankutay1@gmail.com") == null) {
                    List<Team> teams = teamRepository.findAllByRowStatus(RowStatus.ACTIVE);

                    UserDTO user4 = new UserDTO();
                    user4.setEmail("maralyurdakul@gmail.com");
                    user4.setPassword("12345678");
                    user4.setName("maral");
                    user4.setSurname("yurdakul");
                    user4.setTeamId(teams.get(1).getId());
                    user4.setTeamName(teams.get(1).getName());
                    userService.save(user4);

                    UserDTO user = new UserDTO();
                    user.setEmail("yamankutay1@gmail.com");
                    user.setPassword("12345678");
                    user.setName("kutay");
                    user.setSurname("yaman");
                    user.setTeamId(teams.get(1).getId());
                    user.setTeamName(teams.get(1).getName());
                    userService.save(user);

                    UserDTO user2 = new UserDTO();
                    user2.setEmail("batu@gmail.com");
                    user2.setPassword("12345678");
                    user2.setName("batu");
                    user2.setSurname("gokalp");
                    user2.setTeamName(teams.get(0).getName());
                    user2.setTeamId(teams.get(0).getId());
                    userService.save(user2);

                    UserDTO user3 = new UserDTO();
                    user3.setEmail("bahadir@gmail.com");
                    user3.setPassword("12345678");
                    user3.setName("bado");
                    user3.setSurname("gungor");
                    user3.setTeamId(teams.get(0).getId());
                    user3.setTeamName(teams.get(0).getName());
                    userService.save(user3);

                }
            }


            private void createIssues() throws InterruptedException {
                if (issueRepository.countAllByRowStatus(RowStatus.ACTIVE) <= 0) {
                    List<Issue> issueLists = new ArrayList<>();
                    List<JobImplement> jobImplements = jobImplementsRepository.findAllByRowStatus(RowStatus.ACTIVE);
                    for (int i = 0; i < 10; i++) {
                        Issue issue = new Issue();
                        issue.setName("Issue" + (i + 1));
                        issue.setImpactType(ImpactType.LOW);
                        JobImplement jobImplement = jobImplements.get(i % 3);
                        JobInterface jobInterface = jobImplement.getJobInterface();
                        issue.setJobImplement(jobImplement);
                        issue.setDescription(issue.getJobImplement().getId().toString() + " idli Jobda sorun var");
                        Application application = applicationRepository.findFirstByJobInterfaces(jobInterface);
                        issue.setApplication(application);
                        issueLists.add(issue);
                        Thread.sleep(1000);
                    }
                    issueRepository.saveAll(issueLists);
                }
            }
        };
    }
}
