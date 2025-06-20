package tn.esprit.spring;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.Services.Bloc.BlocService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BlocServiceMockTest {
    @Mock
    private BlocRepository blocRepository;
    @Mock
    private ChambreRepository chambreRepository;
    @Mock
    private FoyerRepository foyerRepository;
    private BlocService blocService;

    @BeforeEach
    void beforeEach() {
        blocService = new BlocService(blocRepository, chambreRepository, blocRepository, foyerRepository);
    }

    @AfterEach
    void afterEach() {        // Reset mocks if needed
        reset(blocRepository, chambreRepository, foyerRepository);
    }

    @Order(1)
    @RepeatedTest(4)
    void repeatedTest_example_addOrUpdate() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(101L);
        bloc.setChambres(List.of(chambre));
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);
        Bloc saved = blocService.addOrUpdate(bloc);
        assertNotNull(saved);
        verify(blocRepository, times(1)).save(any(Bloc.class));
        verify(chambreRepository, times(1)).save(any(Chambre.class));
    }

    @Order(2)
    @Test
    void test_deleteById() {
        Bloc bloc = mock(Bloc.class);
        List<Chambre> chambres = new ArrayList<>();
        when(bloc.getChambres()).thenReturn(chambres);
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));
        blocService.deleteById(1L);
        verify(chambreRepository).deleteAll(chambres);
        verify(blocRepository).delete(bloc);
    }

    @Order(3)
    @Test
    void test_affecterBlocAFoyer() {
        String blocName = "Bloc B";
        String foyerName = "Foyer X";
        Bloc bloc = new Bloc();
        bloc.setNomBloc(blocName);
        Foyer foyer = new Foyer();
        foyer.setNomFoyer(foyerName);
        when(blocRepository.findByNomBloc(blocName)).thenReturn(bloc);
        when(foyerRepository.findByNomFoyer(foyerName)).thenReturn(foyer);
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);
        Bloc result = blocService.affecterBlocAFoyer(blocName, foyerName);
        assertEquals(foyer, result.getFoyer());
        verify(blocRepository).save(bloc);
    }

    @Order(4)
    @Test
    void test_findAll() {
        List<Bloc> blocs = List.of(new Bloc(), new Bloc());
        when(blocRepository.findAll()).thenReturn(blocs);
        List<Bloc> result = blocService.findAll();
        assertEquals(2, result.size());
        verify(blocRepository).findAll();
    }
    @Order(5)
    @Test
    void test_delete_blocWithChambres() {
        Bloc bloc = new Bloc();
        Chambre ch1 = new Chambre(); Chambre ch2 = new Chambre();
        bloc.setChambres(List.of(ch1, ch2));

        blocService.delete(bloc);

        verify(chambreRepository).deleteAll(bloc.getChambres());
        verify(blocRepository).delete(bloc);
    }
    @Order(6)
    @Test
    void test_affecterChambresABloc() {
        String blocName = "BlocTest";
        Bloc bloc = new Bloc();
        bloc.setNomBloc(blocName);

        Chambre ch1 = new Chambre(); ch1.setNumeroChambre(101L);
        Chambre ch2 = new Chambre(); ch2.setNumeroChambre(102L);

        when(blocRepository.findByNomBloc(blocName)).thenReturn(bloc);
        when(chambreRepository.findByNumeroChambre(101L)).thenReturn(ch1);
        when(chambreRepository.findByNumeroChambre(102L)).thenReturn(ch2);

        Bloc result = blocService.affecterChambresABloc(List.of(101L, 102L), blocName);

        assertEquals(bloc, result);
        assertEquals(bloc, ch1.getBloc());
        assertEquals(bloc, ch2.getBloc());
        verify(chambreRepository, times(2)).save(any(Chambre.class));
    }

    @Order(7)
    @Test
    void test_ajouterBlocEtSesChambres() {
        Bloc bloc = new Bloc();
        Chambre ch1 = new Chambre(); Chambre ch2 = new Chambre();
        bloc.setChambres(List.of(ch1, ch2));

        Bloc result = blocService.ajouterBlocEtSesChambres(bloc);

        assertEquals(bloc, ch1.getBloc());
        assertEquals(bloc, ch2.getBloc());
        verify(chambreRepository, times(2)).save(any(Chambre.class));
        assertEquals(bloc, result);
    }

    @Order(8)
    @Test
    void test_ajouterBlocEtAffecterAFoyer() {
        Bloc bloc = new Bloc();
        String nomFoyer = "Foyer Test";
        Foyer foyer = new Foyer();
        foyer.setNomFoyer(nomFoyer);

        when(foyerRepository.findByNomFoyer(nomFoyer)).thenReturn(foyer);
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.ajouterBlocEtAffecterAFoyer(bloc, nomFoyer);

        assertEquals(foyer, bloc.getFoyer());
        verify(blocRepository).save(bloc);
        assertEquals(bloc, result);
    }
}