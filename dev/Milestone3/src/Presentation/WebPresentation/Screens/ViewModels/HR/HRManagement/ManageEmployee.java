package Presentation.WebPresentation.Screens.ViewModels.HR.HRManagement;

import Domain.Service.Objects.Employee.Carrier;
import Globals.Enums.Certifications;
import Globals.Enums.JobTitles;
import Globals.Enums.LicenseTypes;
import Globals.util.HumanInteraction;
import Presentation.WebPresentation.Screens.Models.HR.Admin;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.HR_Manager;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.EveryEmployee.EmploymentConds;
import Presentation.WebPresentation.Screens.ViewModels.HR.EveryEmployee.SalaryCalculator;
import Presentation.WebPresentation.Screens.ViewModels.HR.EveryEmployee.UpcomingShifts;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ManageEmployee extends Screen {

    private static final Set<Class<? extends Employee>> ALLOWED
            = new HashSet<>(Arrays.asList(Admin.class, HR_Manager.class));

    private static final String GREET = "Manage Employee";

    public ManageEmployee() {
        super(GREET, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)) {
            redirect(resp, Login.class);
            return;
        }
        header(resp);
        greet(resp);
        printForm(resp, new String[]{"eid"}, new String[]{"Employee ID"}, new String[]{"Manage Employee"});

        if (getParamVal(req, "EmpID") != null){
            try {
                Domain.Service.Objects.Employee.Employee sEmp = controller.getEmployee(getParamVal(req, "EmpID"));
                PrintWriter out = resp.getWriter();
                out.println("<hr>");
                out.println("<form method=\"post\">");
                //ID
                out.println(String.format("<label>ID: %s</label><br><br>", sEmp.id));
                //Name
                out.println("<label>Name: </label>");
                out.println(String.format("<input type=\"text\" name=\"name\" placeholder=\"Employee Name\" value=\"%s\">", sEmp.name));
                out.println("<input type=\"submit\" name=\"uName\" value=\"Update\"><br><br>");
                //Job Title
                out.println(String.format("<label>Title: %s</label><br><br>", sEmp.getType().toString().replaceAll("_", " ")));
                //Starting Date
                out.println(String.format("<label>Starting Date: %s</label><br><br>", sEmp.startingDate.format(HumanInteraction.dateFormat)));
                //Bank Details
                out.println("<label>Bank Details: </label>");
                out.println(String.format("<input type=\"text\" name=\"bDetails\" placeholder=\"Employee Bank Details\" value=\"%s\">", sEmp.bankDetails));
                out.println("<input type=\"submit\" name=\"uBDets\" value=\"Update\"><br><br>");
                //salary
                out.println("<label>Salary: </label>");
                out.println(String.format("<input type=\"text\" name=\"salary\" placeholder=\"Employee Salary\" value=\"%s\">", sEmp.salary));
                out.println("<input type=\"submit\" name=\"uSal\" value=\"Update\"><br><br>");
                //Certificates
                out.println(String.format("<label>Certifications: %s</label><br>", String.join(", ", sEmp.certifications.stream().map(Enum::toString).toArray(String[]::new))));
                out.println("<label>Add Certification: </label>");
                out.println("<select name=\"addCert\">");
                for (Certifications cert : Certifications.values())
                    out.println(String.format("<option value=\"%s\">%s</option>", cert, cert.toString().replaceAll("_", " ")));
                out.println("</select>");
                out.println("<input type=\"submit\" name=\"addCertBut\" value=\"Add\"><br>");
                out.println("<label>Remove Certification: </label>");
                out.println("<select name=\"remCert\">");
                for (Certifications cert : Certifications.values())
                    out.println(String.format("<option value=\"%s\">%s</option>", cert, cert.toString().replaceAll("_", " ")));
                out.println("</select>");
                out.println("<input type=\"submit\" name=\"remCertBut\" value=\"Remove\"><br><br>");
                //Licenses for carrier
                if (sEmp.getType().equals(JobTitles.Carrier)){
                    Carrier sCarrier = (Carrier) sEmp;
                    out.println(String.format("<label>Licenses: %s</label><br>", String.join(", ", sCarrier.licenses.stream().map(Enum::toString).toArray(String[]::new))));
                    out.println("<label>Add License: </label>");
                    out.println("<select name=\"addLicense\">");
                    for (LicenseTypes license : LicenseTypes.values())
                        out.println(String.format("<option value=\"%s\">%s</option>", license, license));
                    out.println("</select>");
                    out.println("<input type=\"submit\" name=\"addLicenseBut\" value=\"Add\"><br>");
                    out.println("<label>Remove License: </label>");
                    out.println("<select name=\"remLicense\">");
                    for (LicenseTypes license : LicenseTypes.values())
                        out.println(String.format("<option value=\"%s\">%s</option>", license, license));
                    out.println("</select>");
                    out.println("<input type=\"submit\" name=\"remLicenseBut\" value=\"Remove\"><br><br>");
                }
                out.println(String.format("<input type=\"submit\" name=\"viewConds\" value=\"View %s's Employment Conditions\"><br><br>", sEmp.name));
                out.println(String.format("<input type=\"submit\" name=\"upcShifts\" value=\"View %s's upcoming shifts\"><br><br>", sEmp.name));
                out.println(String.format("<input type=\"submit\" name=\"salCalc\" value=\"Calculate %s's salary\"><br><br>", sEmp.name));
                out.println(String.format("<input type=\"submit\" name=\"delete\" value=\"Remove %s from the system\"><br><br>", sEmp.name));
                out.println("</form>");

            } catch (Exception e){
                setError(e.getMessage());
            }
        }

        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (handleHeader(req, resp))
            return;
        else if (isButtonPressed(req, "Manage Employee")){
            refresh(req, resp, new String[]{"EmpID"}, new String[]{getParamVal(req, "eid")});
        }
        else if (isButtonPressed(req, "uName")){
            try {
                String newName = getParamVal(req, "name");
                String eid = getParamVal(req, "EmpID");
                controller.editEmployeeName(eid, newName);
                Login.updateUserName(eid);
                setError("Name successfully updated to " + newName);
            } catch (Exception e) {
                setError(e.getMessage());
            }
            refresh(req, resp, new String[]{"EmpID"}, new String[]{getParamVal(req, "EmpID")});
        }
        else if (isButtonPressed(req, "uBDets")){
            try {
                String newBankDetails = getParamVal(req, "bDetails");
                controller.editEmployeeBankDetails(getParamVal(req, "EmpID"), newBankDetails);
                setError("Bank Details successfully updated to " + newBankDetails);
            } catch (Exception e) {
                setError(e.getMessage());
            }
            refresh(req, resp, new String[]{"EmpID"}, new String[]{getParamVal(req, "EmpID")});
        }
        else if (isButtonPressed(req, "uSal")){
            try {
                int newSal = Integer.parseInt(getParamVal(req, "salary"));
                String eid = getParamVal(req, "EmpID");
                controller.editEmployeeSalary(eid, newSal);
                Login.updateSalary(eid);
                setError("Salary successfully updated to " + newSal);
            } catch (NumberFormatException e) {
                setError("Error Occurred: Please enter numeric value for salary");
            } catch (Exception e) {
                setError(e.getMessage());
            }
            refresh(req, resp, new String[]{"EmpID"}, new String[]{getParamVal(req, "EmpID")});
        }
        else if (isButtonPressed(req, "addCertBut")){
            try {
                Domain.Service.Objects.Employee.Employee sEmp = controller.getEmployee(getParamVal(req, "EmpID"));
                Certifications newCert = Certifications.valueOf(getParamVal(req, "addCert"));
                Set<Certifications> newCerts = new HashSet<>(sEmp.certifications);
                newCerts.add(newCert);
                controller.editEmployeeCertifications(sEmp.id, newCerts);
                setError(newCert + " certification added successfully");
            } catch (Exception e) {
                setError(e.getMessage());
            }
            refresh(req, resp, new String[]{"EmpID"}, new String[]{getParamVal(req, "EmpID")});
        }
        else if (isButtonPressed(req, "remCertBut")){
            try {
                Domain.Service.Objects.Employee.Employee sEmp = controller.getEmployee(getParamVal(req, "EmpID"));
                Certifications newCert = Certifications.valueOf(getParamVal(req, "addCert"));
                Set<Certifications> newCerts = new HashSet<>(sEmp.certifications);
                newCerts.remove(newCerts);
                controller.editEmployeeCertifications(sEmp.id, newCerts);
                setError(newCert + " certification removed successfully");
            } catch (Exception e) {
                setError(e.getMessage());
            }
            refresh(req, resp, new String[]{"EmpID"}, new String[]{getParamVal(req, "EmpID")});
        }
        else if (isButtonPressed(req, "addLicenseBut")){
            try {
                Carrier sCarrier = (Carrier)controller.getEmployee(getParamVal(req, "EmpID"));
                LicenseTypes newLicense = LicenseTypes.valueOf(getParamVal(req, "addLicense"));
                Set<LicenseTypes> newLicenses = new HashSet<>(sCarrier.licenses);
                newLicenses.add(newLicense);
                controller.editCarrierLicenses(sCarrier.id, newLicenses);
                setError(newLicense + " license added successfully");
            } catch (Exception e) {
                setError(e.getMessage());
            }
            refresh(req, resp, new String[]{"EmpID"}, new String[]{getParamVal(req, "EmpID")});
        }
        else if (isButtonPressed(req, "remLicenseBut")){
            try {
                Carrier sCarrier = (Carrier)controller.getEmployee(getParamVal(req, "EmpID"));
                LicenseTypes newLicense = LicenseTypes.valueOf(getParamVal(req, "remLicense"));
                Set<LicenseTypes> newLicenses = new HashSet<>(sCarrier.licenses);
                newLicenses.remove(newLicense);
                controller.editCarrierLicenses(sCarrier.id, newLicenses);
                setError(newLicense + " license removed successfully");
            } catch (Exception e) {
                setError(e.getMessage());
            }
            refresh(req, resp, new String[]{"EmpID"}, new String[]{getParamVal(req, "EmpID")});
        }
        else if (isButtonPressed(req, "viewConds")){
            redirect(resp, EmploymentConds.class, new String[]{"EmpID"}, new String[]{getParamVal(req, "EmpID")});
        }
        else if (isButtonPressed(req, "salCalc")){
            redirect(resp, SalaryCalculator.class, new String[]{"EmpID"}, new String[]{getParamVal(req, "EmpID")});
        }
        else if (isButtonPressed(req, "upcShifts")){
            redirect(resp, UpcomingShifts.class, new String[]{"EmpID"}, new String[]{getParamVal(req, "EmpID")});
        } else if (isButtonPressed(req, "delete")) {
            redirect(resp, RemoveEmployee.class, new String[]{"eid"}, new String[]{getParamVal(req, "EmpID")});
        }
    }
}
