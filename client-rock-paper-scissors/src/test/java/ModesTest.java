import org.example.AiVSAi;
import org.example.ResultWaitingForm;
import org.example.SerialCommunicator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
public class ModesTest {

    private SerialCommunicator communicator;
    private AiVSAi aiVSAi;
    private Runnable mockOnGameEnd;
    @BeforeEach
    void setUp() {
        mockOnGameEnd=Mockito.mock(Runnable.class);
        communicator = Mockito.mock(SerialCommunicator.class);

    }

    @Test
    void testPlayGameWinStrategy() {
        AiVSAi aiVSAi = new AiVSAi(communicator,"Ai vs Ai(Win strategy)");


        aiVSAi.PlayGame(mockOnGameEnd);

        // Перевіряємо, що sendMessage викликано з правильним повідомленням
        verify(communicator).sendMessage("Ai vs Ai(Win strategy)\n");
    }

    @Test
    void testPlayGameRandomMove() {
        // Ініціалізуємо клас AiVSAi з "Ai vs Ai(Random move)"
        aiVSAi = new AiVSAi(communicator, "Ai vs Ai(Random move)");

        // Перевіряємо, чи надсилається правильне повідомлення на Arduino
        aiVSAi.PlayGame(mockOnGameEnd);
        verify(communicator).sendMessage("Ai vs Ai(Random move)\n");
    }


}
