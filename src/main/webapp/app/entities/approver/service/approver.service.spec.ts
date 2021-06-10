import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IApprover, Approver } from '../approver.model';

import { ApproverService } from './approver.service';

describe('Service Tests', () => {
  describe('Approver Service', () => {
    let service: ApproverService;
    let httpMock: HttpTestingController;
    let elemDefault: IApprover;
    let expectedResult: IApprover | IApprover[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ApproverService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        programUserId: 'AAAAAAA',
        email: 'AAAAAAA',
        level: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Approver', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Approver()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Approver', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            programUserId: 'BBBBBB',
            email: 'BBBBBB',
            level: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Approver', () => {
        const patchObject = Object.assign(
          {
            programUserId: 'BBBBBB',
            email: 'BBBBBB',
          },
          new Approver()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Approver', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            programUserId: 'BBBBBB',
            email: 'BBBBBB',
            level: 1,
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

      it('should delete a Approver', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addApproverToCollectionIfMissing', () => {
        it('should add a Approver to an empty array', () => {
          const approver: IApprover = { id: 123 };
          expectedResult = service.addApproverToCollectionIfMissing([], approver);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(approver);
        });

        it('should not add a Approver to an array that contains it', () => {
          const approver: IApprover = { id: 123 };
          const approverCollection: IApprover[] = [
            {
              ...approver,
            },
            { id: 456 },
          ];
          expectedResult = service.addApproverToCollectionIfMissing(approverCollection, approver);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Approver to an array that doesn't contain it", () => {
          const approver: IApprover = { id: 123 };
          const approverCollection: IApprover[] = [{ id: 456 }];
          expectedResult = service.addApproverToCollectionIfMissing(approverCollection, approver);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(approver);
        });

        it('should add only unique Approver to an array', () => {
          const approverArray: IApprover[] = [{ id: 123 }, { id: 456 }, { id: 31972 }];
          const approverCollection: IApprover[] = [{ id: 123 }];
          expectedResult = service.addApproverToCollectionIfMissing(approverCollection, ...approverArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const approver: IApprover = { id: 123 };
          const approver2: IApprover = { id: 456 };
          expectedResult = service.addApproverToCollectionIfMissing([], approver, approver2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(approver);
          expect(expectedResult).toContain(approver2);
        });

        it('should accept null and undefined values', () => {
          const approver: IApprover = { id: 123 };
          expectedResult = service.addApproverToCollectionIfMissing([], null, approver, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(approver);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
