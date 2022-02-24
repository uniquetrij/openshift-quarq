import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FileService } from 'app/entities/file/file.service';
import { IFile, File } from 'app/shared/model/file.model';
import { EncryptionAlgorithm } from 'app/shared/model/enumerations/encryption-algorithm.model';

describe('Service Tests', () => {
  describe('File Service', () => {
    let injector: TestBed;
    let service: FileService;
    let httpMock: HttpTestingController;
    let elemDefault: IFile;
    let expectedResult: IFile | IFile[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(FileService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new File(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', false, EncryptionAlgorithm.SHA256);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a File', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new File()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a File', () => {
        const returnedFromService = Object.assign(
          {
            identifier: 'BBBBBB',
            location: 'BBBBBB',
            uri: 'BBBBBB',
            encryption: true,
            encryptionAlgorithm: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of File', () => {
        const returnedFromService = Object.assign(
          {
            identifier: 'BBBBBB',
            location: 'BBBBBB',
            uri: 'BBBBBB',
            encryption: true,
            encryptionAlgorithm: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a File', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
