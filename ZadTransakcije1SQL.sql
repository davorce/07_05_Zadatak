BEGIN TRY 
	BEGIN TRANSACTION
	UPDATE Stavka SET CijenaPoKomadu = CijenaPoKomadu + 10 WHERE IDStavka = 8
	UPDATE Stavka SET CijenaPoKomadu = CijenaPoKomadu - 10 WHERE IDStavka = 9
	COMMIT TRANSACTION
	PRINT 'Transakcija izvrsena!'
END TRY
BEGIN CATCH
	ROLLBACK TRANSACTION
	PRINT 'Transakcija neuspjesna'
	PRINT ERROR_MESSAGE()
END CATCH
