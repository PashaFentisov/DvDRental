CREATE OR REPLACE FUNCTION update_last_update_function()
RETURNS TRIGGER AS $$
BEGIN
    NEW.last_update = now();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_last_update_trigger
    BEFORE UPDATE ON film
    FOR EACH ROW
    EXECUTE FUNCTION update_last_update_function();

CREATE TRIGGER update_last_update_trigger
    BEFORE UPDATE ON country
    FOR EACH ROW
EXECUTE FUNCTION update_last_update_function();

CREATE TRIGGER update_last_update_trigger
    BEFORE UPDATE ON actor
    FOR EACH ROW
EXECUTE FUNCTION update_last_update_function();

CREATE TRIGGER update_last_update_trigger
    BEFORE UPDATE ON address
    FOR EACH ROW
EXECUTE FUNCTION update_last_update_function();

CREATE TRIGGER update_last_update_trigger
    BEFORE UPDATE ON category
    FOR EACH ROW
EXECUTE FUNCTION update_last_update_function();

CREATE TRIGGER update_last_update_trigger
    BEFORE UPDATE ON city
    FOR EACH ROW
EXECUTE FUNCTION update_last_update_function();

CREATE TRIGGER update_last_update_trigger
    BEFORE UPDATE ON customer
    FOR EACH ROW
EXECUTE FUNCTION update_last_update_function();

CREATE TRIGGER update_last_update_trigger
    BEFORE UPDATE ON inventory
    FOR EACH ROW
EXECUTE FUNCTION update_last_update_function();

CREATE TRIGGER update_last_update_trigger
    BEFORE UPDATE ON language
    FOR EACH ROW
EXECUTE FUNCTION update_last_update_function();

CREATE TRIGGER update_last_update_trigger
    BEFORE UPDATE ON rental
    FOR EACH ROW
EXECUTE FUNCTION update_last_update_function();

CREATE TRIGGER update_last_update_trigger
    BEFORE UPDATE ON staff
    FOR EACH ROW
EXECUTE FUNCTION update_last_update_function();

CREATE TRIGGER update_last_update_trigger
    BEFORE UPDATE ON store
    FOR EACH ROW
EXECUTE FUNCTION update_last_update_function();



