Dodałem przycisk, który pozwala wyczyścić cały formularz. Po jego kliknięciu pojawia się okno z pytaniem, czy na pewno chcę usunąć wszystkie dane.

Przed wyczyszczeniem zapisuję wszystkie aktualne wartości w osobnej klasie FormState. Dzięki temu aplikacja pamięta, co wcześniej było wpisane i zaznaczone.

Po potwierdzeniu czyszczę wszystkie pola, ustawiam Spinner na pierwszą pozycję, SeekBar na zero, wyłączam ubezpieczenie i odznaczam regulamin. Usuwam też ewentualne błędy i podsumowanie.

Na końcu pojawia się Snackbar z przyciskiem „Cofnij”. Po jego kliknięciu aplikacja przywraca wcześniej zapisane dane z FormState.

Dzięki temu można cofnąć czyszczenie i odzyskać poprzedni stan formularza.