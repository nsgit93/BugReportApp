package Validator;

import Domain.Tester;

public class ValidatorTester implements IValidator<Tester> {
    @Override
    public void validate(Tester entity) throws ValidationException {
        StringBuffer msg=new StringBuffer();
        if (entity.getSalary() < 2500)
            msg.append("A tester cannot have a salary lower than 3000 M.U.");
        if (msg.length()>0)
            throw new ValidationException(msg.toString());
    }
}
