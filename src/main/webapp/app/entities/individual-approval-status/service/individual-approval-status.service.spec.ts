import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Status } from 'app/entities/enumerations/status.model';
import { IIndividualApprovalStatus, IndividualApprovalStatus } from '../individual-approval-status.model';

import { IndividualApprovalStatusService } from './individual-approval-status.service';

describe('Service Tests', () => {
  describe('IndividualApprovalStatus Service', () => {
    let service: IndividualApprovalStatusService;
    let httpMock: HttpTestingController;
    let elemDefault: IIndividualApprovalStatus;
    let expectedResult: IIndividualApprovalStatus | IIndividualApprovalStatus[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(IndividualApprovalStatusService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        status: Status.INIT,
        clientTime: 'AAAAAAA',
        createdBy: 'AAAAAAA',
        createdAt: currentDate,
        updatedBy: 'AAAAAAA',
        updatedAt: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdAt: currentDate.format(DATE_FORMAT),
            updatedAt: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a IndividualApprovalStatus', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdAt: currentDate.format(DATE_FORMAT),
            updatedAt: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
          },
          returnedFromService
        );

        service.create(new IndividualApprovalStatus()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a IndividualApprovalStatus', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            status: 'BBBBBB',
            clientTime: 'BBBBBB',
            createdBy: 'BBBBBB',
            createdAt: currentDate.format(DATE_FORMAT),
            updatedBy: 'BBBBBB',
            updatedAt: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a IndividualApprovalStatus', () => {
        const patchObject = Object.assign(
          {
            status: 'BBBBBB',
            createdBy: 'BBBBBB',
          },
          new IndividualApprovalStatus()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of IndividualApprovalStatus', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            status: 'BBBBBB',
            clientTime: 'BBBBBB',
            createdBy: 'BBBBBB',
            createdAt: currentDate.format(DATE_FORMAT),
            updatedBy: 'BBBBBB',
            updatedAt: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a IndividualApprovalStatus', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addIndividualApprovalStatusToCollectionIfMissing', () => {
        it('should add a IndividualApprovalStatus to an empty array', () => {
          const individualApprovalStatus: IIndividualApprovalStatus = { id: 123 };
          expectedResult = service.addIndividualApprovalStatusToCollectionIfMissing([], individualApprovalStatus);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(individualApprovalStatus);
        });

        it('should not add a IndividualApprovalStatus to an array that contains it', () => {
          const individualApprovalStatus: IIndividualApprovalStatus = { id: 123 };
          const individualApprovalStatusCollection: IIndividualApprovalStatus[] = [
            {
              ...individualApprovalStatus,
            },
            { id: 456 },
          ];
          expectedResult = service.addIndividualApprovalStatusToCollectionIfMissing(
            individualApprovalStatusCollection,
            individualApprovalStatus
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a IndividualApprovalStatus to an array that doesn't contain it", () => {
          const individualApprovalStatus: IIndividualApprovalStatus = { id: 123 };
          const individualApprovalStatusCollection: IIndividualApprovalStatus[] = [{ id: 456 }];
          expectedResult = service.addIndividualApprovalStatusToCollectionIfMissing(
            individualApprovalStatusCollection,
            individualApprovalStatus
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(individualApprovalStatus);
        });

        it('should add only unique IndividualApprovalStatus to an array', () => {
          const individualApprovalStatusArray: IIndividualApprovalStatus[] = [{ id: 123 }, { id: 456 }, { id: 58734 }];
          const individualApprovalStatusCollection: IIndividualApprovalStatus[] = [{ id: 123 }];
          expectedResult = service.addIndividualApprovalStatusToCollectionIfMissing(
            individualApprovalStatusCollection,
            ...individualApprovalStatusArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const individualApprovalStatus: IIndividualApprovalStatus = { id: 123 };
          const individualApprovalStatus2: IIndividualApprovalStatus = { id: 456 };
          expectedResult = service.addIndividualApprovalStatusToCollectionIfMissing(
            [],
            individualApprovalStatus,
            individualApprovalStatus2
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(individualApprovalStatus);
          expect(expectedResult).toContain(individualApprovalStatus2);
        });

        it('should accept null and undefined values', () => {
          const individualApprovalStatus: IIndividualApprovalStatus = { id: 123 };
          expectedResult = service.addIndividualApprovalStatusToCollectionIfMissing([], null, individualApprovalStatus, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(individualApprovalStatus);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
