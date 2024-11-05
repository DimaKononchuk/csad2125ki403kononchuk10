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
    @BeforeEach
    void setUp() {
        communicator = Mockito.mock(SerialCommunicator.class);
    }

    @Test
    void testPlayGameWinStrategy() {
        // Ініціалізуємо клас AiVSAi з "Ai vs Ai(Win strategy)"
        aiVSAi = new AiVSAi(communicator, "Ai vs Ai(Win strategy)");

        // Перевіряємо, чи надсилається правильне повідомлення на Arduino
        aiVSAi.PlayGame(null);

        verify(communicator).sendMessage("Ai vs Ai(Win strategy)\n");
    }

    @Test
    void testPlayGameRandomMove() {
        // Ініціалізуємо клас AiVSAi з "Ai vs Ai(Random move)"
        aiVSAi = new AiVSAi(communicator, "Ai vs Ai(Random move)");

        // Перевіряємо, чи надсилається правильне повідомлення на Arduino
        aiVSAi.PlayGame(null);
        verify(communicator).sendMessage("Ai vs Ai(Random move)\n");
    }


}
