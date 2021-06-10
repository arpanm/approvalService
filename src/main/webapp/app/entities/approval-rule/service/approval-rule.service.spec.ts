import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApprovalType } from 'app/entities/enumerations/approval-type.model';
import { IApprovalRule, ApprovalRule } from '../approval-rule.model';

import { ApprovalRuleService } from './approval-rule.service';

describe('Service Tests', () => {
  describe('ApprovalRule Service', () => {
    let service: ApprovalRuleService;
    let httpMock: HttpTestingController;
    let elemDefault: IApprovalRule;
    let expectedResult: IApprovalRule | IApprovalRule[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ApprovalRuleService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        programId: 'AAAAAAA',
        type: ApprovalType.Job,
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

      it('should create a ApprovalRule', () => {
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

        service.create(new ApprovalRule()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ApprovalRule', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            programId: 'BBBBBB',
            type: 'BBBBBB',
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

      it('should partial update a ApprovalRule', () => {
        const patchObject = Object.assign(
          {
            programId: 'BBBBBB',
            type: 'BBBBBB',
            createdBy: 'BBBBBB',
          },
          new ApprovalRule()
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

      it('should return a list of ApprovalRule', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            programId: 'BBBBBB',
            type: 'BBBBBB',
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

      it('should delete a ApprovalRule', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addApprovalRuleToCollectionIfMissing', () => {
        it('should add a ApprovalRule to an empty array', () => {
          const approvalRule: IApprovalRule = { id: 123 };
          expectedResult = service.addApprovalRuleToCollectionIfMissing([], approvalRule);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(approvalRule);
        });

        it('should not add a ApprovalRule to an array that contains it', () => {
          const approvalRule: IApprovalRule = { id: 123 };
          const approvalRuleCollection: IApprovalRule[] = [
            {
              ...approvalRule,
            },
            { id: 456 },
          ];
          expectedResult = service.addApprovalRuleToCollectionIfMissing(approvalRuleCollection, approvalRule);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ApprovalRule to an array that doesn't contain it", () => {
          const approvalRule: IApprovalRule = { id: 123 };
          const approvalRuleCollection: IApprovalRule[] = [{ id: 456 }];
          expectedResult = service.addApprovalRuleToCollectionIfMissing(approvalRuleCollection, approvalRule);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(approvalRule);
        });

        it('should add only unique ApprovalRule to an array', () => {
          const approvalRuleArray: IApprovalRule[] = [{ id: 123 }, { id: 456 }, { id: 10255 }];
          const approvalRuleCollection: IApprovalRule[] = [{ id: 123 }];
          expectedResult = service.addApprovalRuleToCollectionIfMissing(approvalRuleCollection, ...approvalRuleArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const approvalRule: IApprovalRule = { id: 123 };
          const approvalRule2: IApprovalRule = { id: 456 };
          expectedResult = service.addApprovalRuleToCollectionIfMissing([], approvalRule, approvalRule2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(approvalRule);
          expect(expectedResult).toContain(approvalRule2);
        });

        it('should accept null and undefined values', () => {
          const approvalRule: IApprovalRule = { id: 123 };
          expectedResult = service.addApprovalRuleToCollectionIfMissing([], null, approvalRule, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(approvalRule);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
